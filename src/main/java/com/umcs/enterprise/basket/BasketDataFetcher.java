package com.umcs.enterprise.basket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.netflix.graphql.dgs.*;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import com.umcs.enterprise.ConnectionService;
import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.node.GlobalId;
import com.umcs.enterprise.types.BasketBookInput;
import com.umcs.enterprise.types.BasketBookResult;
import com.umcs.enterprise.types.UnbasketBookInput;
import com.umcs.enterprise.types.UnbasketBookResult;
import graphql.relay.Connection;
import graphql.relay.DefaultConnectionCursor;
import java.util.Objects;
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
	private final ConnectionService connectionService;

	@DgsData(parentType = "Basket")
	public Connection<BookEdge> books(DgsDataFetchingEnvironment env) {
		Basket basket = env.getSource();

		return connectionService.getConnection(basket.getBooks(), env);
	}

	@NonNull
	private final BasketService basketService;

	@NonNull
	private final SummableService summableService;

	@DgsData(parentType = "Basket")
	public Long price(DgsDataFetchingEnvironment env) {
		Basket basket = env.getSource();

		return summableService.sumPrice(basket.getBooks());
	}

	@DgsData(parentType = "Basket")
	public Integer quantity(DgsDataFetchingEnvironment env) {
		Basket basket = env.getSource();
		return summableService.sumQuantity(basket.getBooks());
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

	@DgsMutation
	public BasketBookResult basketBook(
		@InputArgument BasketBookInput input,
		@RequestHeader(required = false) String Authorization,
		DgsDataFetchingEnvironment dfe
	) throws JsonProcessingException {
		GlobalId<Long> globalId = GlobalId.from(
			input.getBook().getId(),
			new TypeReference<GlobalId<Long>>() {}
		);
		assert Objects.equals(globalId.className(), "Book");

		Book book = bookRepository
			.findById((globalId.databaseId()))
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
		GlobalId<Long> globalId = GlobalId.from(
			input.getBook().getId(),
			new TypeReference<GlobalId<Long>>() {}
		);
		assert Objects.equals(globalId.className(), "Book");

		Book book = bookRepository
			.findById((globalId.databaseId()))
			.orElseThrow(DgsEntityNotFoundException::new);

		Basket basket = basketService.getBasket(input.getBasket().getId());
		com.umcs.enterprise.basket.BookEdge edge = basket.remove(book);

		return UnbasketBookResult
			.newBuilder()
			.edge(
				edge == null
					? null
					: new graphql.relay.DefaultEdge<>(
						edge,
						new DefaultConnectionCursor(edge.getBook().getId())
					)
			)
			.basket(basket)
			.build();
	}
}
