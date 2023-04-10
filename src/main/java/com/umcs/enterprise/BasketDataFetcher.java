package com.umcs.enterprise;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.*;
import com.netflix.graphql.dgs.internal.DgsWebMvcRequestData;
import com.umcs.enterprise.types.*;
import graphql.relay.Connection;
import graphql.relay.DefaultEdge;
import graphql.schema.DataFetchingEnvironment;
import jakarta.servlet.http.Cookie;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.context.request.ServletWebRequest;

@DgsComponent
@RequiredArgsConstructor
public class BasketDataFetcher {

	private final BookRepository bookRepository;

	private Map<Long, Integer> getBasket(String basket) throws JsonProcessingException {
		if (basket == null) {
			return new HashMap<>();
		}

		try {
			return new ObjectMapper()
				.readValue(
					new String(Base64.getDecoder().decode(basket.getBytes()), StandardCharsets.UTF_8),
					new TypeReference<>() {}
				);
		} catch (IllegalArgumentException e) {
			return new HashMap<>();
		}
	}

	@DgsQuery
	public Map<Long, Integer> basket(@CookieValue(required = false) String basket)
		throws JsonProcessingException {
		return getBasket(basket);
	}

	@DgsData(parentType = "Basket")
	public CompletableFuture<String> totalPrice(
		DgsDataFetchingEnvironment enf,
		DataFetchingEnvironment env
	) {
		DataLoader<Long, Book> dataLoader = enf.getDataLoader(BookDataLoader.class);
		Locale poland = new Locale("pl", "PL");

		Map<Long, Integer> basket = env.<Map<Long, Integer>>getSource();

		return dataLoader
			.loadMany(basket.keySet().stream().toList())
			.thenApply(books ->
				books
					.stream()
					.filter(Objects::nonNull)
					.map(book ->
						book.getPrice().multiply(BigDecimal.valueOf(basket.get(book.getDatabaseId())))
					)
					.reduce(BigDecimal.ZERO, BigDecimal::add)
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
			.loadMany(env.<Map<Long, Integer>>getSource().keySet().stream().toList())
			.thenApply(books -> connectionService.getConnection(books, env));
	}

	@DgsData(parentType = "BasketBooksEdge")
	public Integer quantity(
		@CookieValue(required = false) String basket,
		DataFetchingEnvironment env
	) throws JsonProcessingException {
		return getBasket(basket).get(env.<DefaultEdge<Book>>getSource().getNode().getDatabaseId());
	}

	private void addCookie(DgsDataFetchingEnvironment dfe, Cookie cookie) {
		DgsWebMvcRequestData requestData = (DgsWebMvcRequestData) dfe.getDgsContext().getRequestData();
		ServletWebRequest webRequest = (ServletWebRequest) requestData.getWebRequest();

		cookie.setHttpOnly(true);
		cookie.setSecure(true);

		webRequest.getResponse().addCookie(cookie);
	}

	@DgsMutation
	public Map<Long, Integer> basketBook(
		@InputArgument BasketBookInput input,
		@CookieValue(required = false) String basket,
		DgsDataFetchingEnvironment dfe
	) throws JsonProcessingException {
		Map<Long, Integer> books = getBasket(basket);

		GlobalId globalId = GlobalId.from(input.getId());
		assert Objects.equals(globalId.className(), "Book");
		books.merge(globalId.databaseId(), 1, Integer::sum);

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

		return books;
	}

	@DgsData.List(
		{ @DgsData(parentType = "UnbasketBookResult"), @DgsData(parentType = "BasketBookResult") }
	)
	public Map<Long, Integer> basket(DataFetchingEnvironment env) {
		return env.<Map<Long, Integer>>getRoot();
	}

	@DgsMutation
	public Map<Long, Integer> unbasketBook(
		@InputArgument UnbasketBookInput input,
		@CookieValue(required = false) String basket,
		DgsDataFetchingEnvironment dfe
	) throws JsonProcessingException {
		Map<Long, Integer> books = getBasket(basket);

		GlobalId globalId = GlobalId.from(input.getId());
		assert Objects.equals(globalId.className(), "Book");

		books.computeIfPresent(
			globalId.databaseId(),
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

		return books;
	}
}