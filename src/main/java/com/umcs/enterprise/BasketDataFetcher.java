package com.umcs.enterprise;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.*;
import com.netflix.graphql.dgs.internal.DgsRequestData;
import com.netflix.graphql.dgs.internal.DgsWebMvcRequestData;
import com.umcs.enterprise.types.*;
import graphql.relay.Connection;
import graphql.relay.DefaultEdge;
import graphql.relay.SimpleListConnection;
import graphql.schema.DataFetchingEnvironment;
import jakarta.servlet.http.Cookie;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.context.request.ServletWebRequest;

@DgsComponent
@RequiredArgsConstructor
public class BasketDataFetcher {

	private final BookRepository bookRepository;

	@DgsQuery
	public List<Long> basket(@CookieValue(required = false) String basket)
		throws JsonProcessingException {
		if (basket == null) {
			return new ArrayList<>();
		}

		Map<Long, Integer> books = new ObjectMapper()
			.readValue(
				new String(Base64.getDecoder().decode(basket.getBytes()), StandardCharsets.UTF_8),
				new TypeReference<Map<Long, Integer>>() {}
			);

		return books.keySet().stream().toList();
	}

	@DgsData(parentType = "Basket")
	public CompletableFuture<String> totalPrice(
		DgsDataFetchingEnvironment enf,
		DataFetchingEnvironment env
	) {
		DataLoader<Long, Book> dataLoader = enf.getDataLoader(BookDataLoader.class);
		Locale poland = new Locale("pl", "PL");

		return dataLoader
			.loadMany(env.<List<Long>>getSource())
			.thenApply(books ->
				books.stream().map(Book::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add)
			)
			.thenApply(NumberFormat.getCurrencyInstance(poland)::format);
	}

	private final ConnectionService connectionService;

	@DgsData(parentType = "Basket")
	public CompletableFuture<Connection<Book>> books(
		DgsDataFetchingEnvironment enf,
		DataFetchingEnvironment env
	) {
		DataLoader<Long, Book> dataLoader = enf.getDataLoader(BookDataLoader.class);

		return dataLoader
			.loadMany(env.<List<Long>>getSource())
			.thenApply(books -> connectionService.getConnection(books, env));
	}

	@DgsData(parentType = "BasketBooksEdge")
	public Integer quantity(
		@CookieValue(required = false) String basket,
		DataFetchingEnvironment env
	) throws JsonProcessingException {
		if (basket == null) {
			return null;
		}

		Map<Long, Integer> books = new ObjectMapper()
			.readValue(
				new String(Base64.getDecoder().decode(basket.getBytes()), StandardCharsets.UTF_8),
				new TypeReference<Map<Long, Integer>>() {}
			);

		return books.get(env.<DefaultEdge<Book>>getSource().getNode().getDatabaseId());
	}

	private void addCookie(DgsDataFetchingEnvironment dfe, Cookie cookie) {
		DgsWebMvcRequestData requestData = (DgsWebMvcRequestData) dfe.getDgsContext().getRequestData();
		ServletWebRequest webRequest = (ServletWebRequest) requestData.getWebRequest();

		cookie.setHttpOnly(true);
		cookie.setSecure(true);

		webRequest.getResponse().addCookie(cookie);
	}

	@DgsMutation
	public List<Long> basketBook(
		@InputArgument BasketBookInput input,
		@CookieValue(required = false) String basket,
		DgsDataFetchingEnvironment dfe
	) throws JsonProcessingException {
		Map<Long, Integer> books = basket == null
			? new HashMap<>()
			: new ObjectMapper()
				.readValue(
					new String(Base64.getDecoder().decode(basket.getBytes()), StandardCharsets.UTF_8),
					new TypeReference<Map<Long, Integer>>() {}
				);

		GlobalId globalId = GlobalId.from(input.getId());
		assert Objects.equals(globalId.getClassName(), "Book");
		books.merge(globalId.getDatabaseId(), 1, (k, v) -> v + 1);

		Cookie cookie = new Cookie(
			"basket",
			Base64
				.getEncoder()
				.encodeToString(
					new ObjectMapper().writeValueAsString(books).getBytes(StandardCharsets.UTF_8)
				)
		);
		cookie.setPath("/");
		addCookie(dfe, cookie);

		return books.keySet().stream().toList();
	}

	@DgsData.List(
		{ @DgsData(parentType = "UnbasketBookResult"), @DgsData(parentType = "BasketBookResult") }
	)
	public List<Long> basket(DataFetchingEnvironment env) {
		return env.<List<Long>>getRoot();
	}

	@DgsMutation
	public List<Long> unbasketBook(
		@InputArgument UnbasketBookInput input,
		@CookieValue(required = false) String basket,
		DgsDataFetchingEnvironment dfe
	) throws JsonProcessingException {
		if (basket == null) {
			return new ArrayList<>();
		}

		Map<Long, Integer> books = new ObjectMapper()
			.readValue(
				new String(Base64.getDecoder().decode(basket.getBytes()), StandardCharsets.UTF_8),
				new TypeReference<Map<Long, Integer>>() {}
			);

		GlobalId globalId = GlobalId.from(input.getId());
		assert Objects.equals(globalId.getClassName(), "Book");

		books.computeIfPresent(
			globalId.getDatabaseId(),
			(k, v) -> {
				if (v < 2) {
					return null;
				}
				return v - 1;
			}
		);

		Cookie cookie = new Cookie(
			"basket",
			Base64
				.getEncoder()
				.encodeToString(
					new ObjectMapper().writeValueAsString(books).getBytes(StandardCharsets.UTF_8)
				)
		);
		cookie.setPath("/");
		addCookie(dfe, cookie);

		return books.keySet().stream().toList();
	}
}
