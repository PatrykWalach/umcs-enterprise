package com.umcs.enterprise.basket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.netflix.graphql.dgs.*;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.node.GlobalId;
import com.umcs.enterprise.types.*;
import graphql.relay.DefaultConnectionCursor;
import java.math.BigDecimal;
import java.util.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestHeader;

@DgsComponent
@RequiredArgsConstructor
public class BasketDataFetcher {

	@DgsQuery
	public Basket basket(String id) throws JsonProcessingException {
		return basketService.getBasket(id);
	}

	@NonNull
	private final BookRepository bookRepository;

	@NonNull
	private final BasketService basketService;

	@DgsData(parentType = "Basket")
	public BigDecimal price(DgsDataFetchingEnvironment env) {
		Basket basket = env.getSource();

		return basket
			.getBooks()
			.stream()
			.filter(Objects::nonNull)
			.map(edge -> edge.getBook().getPrice().multiply(BigDecimal.valueOf(edge.getQuantity())))
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	@DgsData(parentType = "Book")
	public Boolean inBasket(DgsDataFetchingEnvironment env, String id)
		throws JsonProcessingException {
		com.umcs.enterprise.book.Book book = env.getSource();
		return basketService
			.getBasket(id)
			.getBooks()
			.stream()
			.map(BookEdge::getBook)
			.anyMatch(book::equals);
	}

	@DgsData(parentType = "Basket")
	public Integer quantity(DgsDataFetchingEnvironment env) {
		Basket basket = env.getSource();
		return basket.getBooks().stream().mapToInt(BookEdge::getQuantity).sum();
	}

	@DgsMutation
	public BasketBookResult basketBook(
		@InputArgument BasketBookInput input,
		@RequestHeader(required = false) String Authorization,
		DgsDataFetchingEnvironment dfe
	) throws JsonProcessingException {
		GlobalId<String> globalId = GlobalId.from(input.getBook().getId());
		assert Objects.equals(globalId.className(), "Book");

		Book book = bookRepository
			.findById(UUID.fromString(globalId.databaseId()))
			.orElseThrow(DgsEntityNotFoundException::new);

		Basket basket = basketService.getBasket(input.getBasket().getId());
		com.umcs.enterprise.basket.BookEdge edge = basket.add(book);

		return BasketBookResult
			.newBuilder()
			.edge(
				new graphql.relay.DefaultEdge<>(edge, new DefaultConnectionCursor(edge.getBook().getId()))
			)
			.basket(basket)
			.build();
	}

	@DgsMutation
	public UnbasketBookResult unbasketBook(
		@InputArgument UnbasketBookInput input,
		@RequestHeader(required = false) String Authorization,
		DgsDataFetchingEnvironment dfe
	) throws JsonProcessingException {
		GlobalId<String> globalId = GlobalId.from(input.getBook().getId());
		assert Objects.equals(globalId.className(), "Book");

		Book book = bookRepository
			.findById(UUID.fromString(globalId.databaseId()))
			.orElseThrow(DgsEntityNotFoundException::new);

		Basket basket = basketService.getBasket(input.getBasket().getId());
		com.umcs.enterprise.basket.BookEdge edge = basket.remove(book);

		return UnbasketBookResult
			.newBuilder()
			.edge(
				new graphql.relay.DefaultEdge<>(edge, new DefaultConnectionCursor(edge.getBook().getId()))
			)
			.basket(basket)
			.build();
	}
}
