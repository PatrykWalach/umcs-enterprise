package com.umcs.enterprise.basket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.netflix.graphql.dgs.*;
import com.umcs.enterprise.auth.JwtService;
import com.umcs.enterprise.book.BookDataLoader;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.node.GlobalId;
import com.umcs.enterprise.types.*;
import java.math.BigDecimal;
import java.util.*;

import graphql.relay.DefaultConnectionCursor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestHeader;

@DgsComponent
@RequiredArgsConstructor
public class BasketDataFetcher {

	@Value("${spring.security.authentication.jwt.secret}")
	private String secret;

	@DgsQuery
	public Basket basket(@RequestHeader(required = false) String Authorization)
		throws JsonProcessingException {
		return getBasketService(Authorization).getBasket();
	}

	@NonNull
	private final JwtService jwtService;

	@NonNull
	private final BookRepository bookRepository;

	@NonNull
	private final BasketRepository basketRepository;

	@NonNull
	private final BookEdgeRepository bookEdgeRepository;

	private BasketService getBasketService(String Authorization) {
		if (
			Authorization != null &&
			jwtService.parseAuthorizationHeader(Authorization).getSubject() != null
		) {
			return new UserBasketService(
				jwtService,
				basketRepository,
				bookEdgeRepository,
				Authorization,
				bookRepository
			);
		}

		return new AnonymousBasketService(jwtService, Authorization, bookRepository);
	}

	@DgsData(parentType = "Basket")
	public BigDecimal price(DgsDataFetchingEnvironment env) {
		DataLoader<UUID, com.umcs.enterprise.book.Book> dataLoader = env.getDataLoader(
			BookDataLoader.class
		);

		Basket basket = env.getSource();

		return basket
			.getBooks()
			.stream()
			.filter(Objects::nonNull)
			.map(edge -> edge.getBook().getPrice().multiply(BigDecimal.valueOf(edge.getQuantity())))
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}


	@DgsData(parentType = "Book")
	public Boolean inBasket(DgsDataFetchingEnvironment env, 	@RequestHeader(required = false) String Authorization) throws JsonProcessingException {
		Basket basket = getBasketService(Authorization).getBasket();

		com.umcs.enterprise.book.Book book = env.getSource();

		return basket
				.getBooks()
				.stream().map(BookEdge::getBook)
				.anyMatch(book::equals);
	}

	@DgsData(parentType = "Basket")
	public Integer quantity(DgsDataFetchingEnvironment env) {
		Basket basket = env.getSource();
		return basket.getBooks().size();
	}

	@DgsMutation
	public BasketBookResult basketBook(
		@InputArgument BasketBookInput input,
		@RequestHeader(required = false) String Authorization,
		DgsDataFetchingEnvironment dfe
	) throws JsonProcessingException {
		GlobalId globalId = GlobalId.from(input.getBook().getId());
		assert Objects.equals(globalId.className(), "Book");

		BasketService service = getBasketService(Authorization);
		com.umcs.enterprise.basket.BookEdge edge = service.basketBook(globalId.databaseId());

		return BasketBookResult.newBuilder().edge(new graphql.relay.DefaultEdge<>(
				edge,
				new DefaultConnectionCursor(edge.getBook().getId())
		)).token(service.getToken()).basket(service.getBasket()).build();
	}

	@DgsMutation
	public UnbasketBookResult unbasketBook(
		@InputArgument UnbasketBookInput input,
		@RequestHeader(required = false) String Authorization,
		DgsDataFetchingEnvironment dfe
	) throws JsonProcessingException {
		GlobalId globalId = GlobalId.from(input.getBook().getId());
		assert Objects.equals(globalId.className(), "Book");

		BasketService service = getBasketService(Authorization);

		com.umcs.enterprise.basket.BookEdge edge = service.unbasketBook(globalId.databaseId());


		return UnbasketBookResult.newBuilder().edge(new graphql.relay.DefaultEdge<>(
				edge,
				new DefaultConnectionCursor(edge.getBook().getId())
		)).token(service.getToken()).basket(service.getBasket()).build();
	}
}
