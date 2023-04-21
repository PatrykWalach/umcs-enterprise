package com.umcs.enterprise.basket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.*;
import com.netflix.graphql.dgs.internal.DgsWebMvcRequestData;
import com.umcs.enterprise.auth.JwtService;
import com.umcs.enterprise.auth.UserDetailsService;
import com.umcs.enterprise.book.BookDataLoader;
import com.umcs.enterprise.node.GlobalId;
import com.umcs.enterprise.types.*;
import graphql.schema.DataFetchingEnvironment;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import javax.crypto.SecretKey;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.context.request.ServletWebRequest;

@DgsComponent
@RequiredArgsConstructor
public class BasketDataFetcher {

	@Value("${spring.security.authentication.jwt.secret}")
	private String secret;

	@DgsQuery
	public Map<UUID, Integer> basket(@RequestHeader(required = false) String Authorization)
		throws JsonProcessingException {
		return basketService.getBasket(Authorization);
	}

	@DgsData(parentType = "Basket")
	public CompletableFuture<BigDecimal> price(DgsDataFetchingEnvironment env) {
		DataLoader<UUID, com.umcs.enterprise.book.Book> dataLoader = env.getDataLoader(
			BookDataLoader.class
		);

		Map<UUID, Integer> basket = env.getSource();

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
			);
	}

	@DgsData(parentType = "Basket")
	public Integer quantity(DgsDataFetchingEnvironment env) {
		Map<Long, Integer> basket = env.getSource();
		return basket.values().stream().reduce(0, Integer::sum);
	}

	@DgsData(parentType = "Basket")
	public String id(DataFetchingEnvironment env) throws JsonProcessingException {
		return Base64
			.getEncoder()
			.encodeToString(
				new ObjectMapper()
					.writeValueAsString(env.<Map<Long, Integer>>getSource())
					.getBytes(StandardCharsets.UTF_8)
			);
	}

	private String setBasket(Map<UUID, Integer> basket) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(basket);
	}

	@DgsMutation
	public BasketBookResult basketBook(
		@InputArgument BasketBookInput input,
		@RequestHeader(required = false) String Authorization,
		DgsDataFetchingEnvironment dfe
	) throws JsonProcessingException {
		Map<UUID, Integer> books = basketService.getBasket(Authorization);

		GlobalId globalId = GlobalId.from(input.getBook().getId());
		assert Objects.equals(globalId.className(), "Book");
		books.merge(globalId.databaseId(), 1, Integer::sum);

		return BasketBookResult
			.newBuilder()
			.basket(books)
			.token(basketService.setBasket(Authorization, books))
			.build();
	}

	@NonNull
	private final BasketService basketService;

	@DgsMutation
	public UnbasketBookResult unbasketBook(
		@InputArgument UnbasketBookInput input,
		@RequestHeader(required = false) String Authorization,
		DgsDataFetchingEnvironment dfe
	) throws JsonProcessingException {
		Map<UUID, Integer> books = basketService.getBasket(Authorization);

		GlobalId globalId = GlobalId.from(input.getBook().getId());
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

		return UnbasketBookResult
			.newBuilder()
			.basket(books)
			.token(basketService.setBasket(Authorization, books))
			.build();
	}

	private final JwtService jwtService;
}
