package com.umcs.enterprise.basket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.*;
import com.umcs.enterprise.book.BookDataLoader;
import com.umcs.enterprise.node.GlobalId;
import com.umcs.enterprise.types.*;
import graphql.schema.DataFetchingEnvironment;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;

@DgsComponent
@RequiredArgsConstructor
public class BasketDataFetcher {

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
	public Map<Long, Integer> basket(@InputArgument String id) throws JsonProcessingException {
		return getBasket(id);
	}

	@DgsData(parentType = "Basket")
	public CompletableFuture<BigDecimal> price(
		DgsDataFetchingEnvironment enf,
		DataFetchingEnvironment env
	) {
		DataLoader<Long, com.umcs.enterprise.book.Book> dataLoader = enf.getDataLoader(
			BookDataLoader.class
		);

		Map<Long, Integer> basket = env.getSource();

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
	public String id(DataFetchingEnvironment env) throws JsonProcessingException {
		return Base64
			.getEncoder()
			.encodeToString(
				new ObjectMapper()
					.writeValueAsString(env.<Map<Long, Integer>>getSource())
					.getBytes(StandardCharsets.UTF_8)
			);
	}

	@DgsMutation
	public Map<Long, Integer> basketBook(@InputArgument BasketBookInput input)
		throws JsonProcessingException {
		Map<Long, Integer> books = getBasket(input.getBasket().getId());

		GlobalId globalId = GlobalId.from(input.getBook().getId());
		assert Objects.equals(globalId.className(), "Book");
		books.merge(globalId.databaseId(), 1, Integer::sum);

		return books;
	}

	@DgsData.List(
		{ @DgsData(parentType = "UnbasketBookResult"), @DgsData(parentType = "BasketBookResult") }
	)
	public Map<Long, Integer> basket(DataFetchingEnvironment env) {
		return env.getSource();
	}

	@DgsMutation
	public Map<Long, Integer> unbasketBook(@InputArgument UnbasketBookInput input)
		throws JsonProcessingException {
		Map<Long, Integer> books = getBasket(input.getBasket().getId());

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

		return books;
	}
}
