package com.umcs.enterprise.basket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umcs.enterprise.auth.JwtService;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.types.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.NonNull;

public class AnonymousBasketService implements BasketService {

	public AnonymousBasketService(
		@NonNull JwtService jwtService,
		String authorization,
		@NonNull BookRepository bookRepository
	) {
		this.jwtService = jwtService;
		Authorization = authorization;
		this.bookRepository = bookRepository;
	}

	@NonNull
	private final JwtService jwtService;

	private String Authorization;

	private Token token;

	private void setBasket(Map<UUID, Integer> basket) throws JsonProcessingException {
		OffsetDateTime expiresAt = (OffsetDateTime.now().plusSeconds(60 * 60 * 24 * 365));

		JwtBuilder builder =
			(
				Jwts
					.builder()
					.setClaims(
						Map.of(
							"http://localhost:8080/graphql/basket",
							new ObjectMapper().writeValueAsString(basket)
						)
					)
			).setExpiration(Date.from(expiresAt.toInstant()));

		String token = jwtService.signToken(builder);
		this.Authorization = "Bearer " + token;

		this.token = Token.newBuilder().value(token).expiresAt(expiresAt).build();
	}

	@NonNull
	private final BookRepository bookRepository;

	@Override
	@NonNull
	public Basket getBasket() throws JsonProcessingException {
		if (Authorization == null) {
			return Basket.newBuilder().books(new ArrayList<>()).build();
		}

		try {
			Claims token = jwtService.parseAuthorizationHeader(Authorization);

			String basket = token.get("http://localhost:8080/graphql/basket", String.class);

			if (basket == null) {
				return Basket.newBuilder().books(new ArrayList<>()).build();
			}

			Map<UUID, Integer> parsed = new ObjectMapper().readValue(basket, new TypeReference<>() {});

			return Basket
				.newBuilder()
				.books(
					bookRepository
						.findAllById(parsed.keySet())
						.stream()
						.map(e -> BookEdge.newBuilder().book(e).quantity(parsed.get(e.getDatabaseId())).build())
						.toList()
				)
				.build();
		} catch (IllegalArgumentException e) {
			return Basket.newBuilder().books(new ArrayList<>()).build();
		}
	}

	@Override
	public Token getToken() throws JsonProcessingException {
		return this.token;
	}

	@Override
	public BookEdge basketBook(@NonNull UUID databaseId) throws JsonProcessingException {
		Basket basket = getBasket();
		Map<UUID, Integer> books = basket
			.getBooks()
			.stream()
			.collect(Collectors.toMap(e -> e.getBook().getDatabaseId(), BookEdge::getQuantity));

		Integer quantity = books.merge(databaseId, 1, Integer::sum);
		setBasket(books);

		return BookEdge
			.newBuilder()
			.basket(getBasket())
				.quantity(quantity)
			.book(bookRepository.findById(databaseId).orElse(null))
			.build();
	}

	@Override
	public @NonNull BookEdge unbasketBook(@NonNull UUID databaseId) throws JsonProcessingException {
		Basket basket = getBasket();
		Map<UUID, Integer> books = basket
			.getBooks()
			.stream()
			.collect(Collectors.toMap(e -> e.getBook().getDatabaseId(), BookEdge::getQuantity));

		books.computeIfPresent(
			databaseId,
			(key, value) -> {
				if (value < 2) {
					return null;
				}
				return value - 1;
			}
		);
		setBasket(books);

		return BookEdge
			.newBuilder()
			.basket(basket)
			.book(bookRepository.findById(databaseId).orElse(null))
			.build();
	}
}
