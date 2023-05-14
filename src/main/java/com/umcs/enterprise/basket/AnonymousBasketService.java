package com.umcs.enterprise.basket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umcs.enterprise.book.BookRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;

@AllArgsConstructor
public class AnonymousBasketService implements BasketService {

	@NonNull
	private final OAuth2AccessTokenGenerator jwtService;

	private String id;

	private String setBasket(Map<UUID, Integer> basket) throws JsonProcessingException {
		id = new ObjectMapper().writeValueAsString(basket);
		return id;
	}

	@NonNull
	private final BookRepository bookRepository;

	@Override
	@NonNull
	public Basket getBasket() throws JsonProcessingException {
		if (id == null) {
			return Basket.newBuilder().books(new ArrayList<>()).build();
		}

		try {
			Map<UUID, Integer> parsed = new ObjectMapper().readValue(id, new TypeReference<>() {});

			return Basket
				.newBuilder()
				.books(
					bookRepository
						.findAllById(parsed.keySet())
						.stream()
						.map(e -> BookEdge.newBuilder().book(e).quantity(parsed.get(e.getDatabaseId())).build())
						.collect(Collectors.toList())
				)
				.build();
		} catch (IllegalArgumentException e) {
			return Basket.newBuilder().books(new ArrayList<>()).build();
		}
	}

	@Override
	public @NonNull String basketBook(@NonNull UUID databaseId) throws JsonProcessingException {
		Basket basket = getBasket();
		Map<UUID, Integer> books = basket
			.getBooks()
			.stream()
			.collect(Collectors.toMap(e -> e.getBook().getDatabaseId(), BookEdge::getQuantity));
		books.merge(databaseId, 1, Integer::sum);
		return setBasket(books);
	}

	@Override
	public @NonNull String unbasketBook(@NonNull UUID databaseId) throws JsonProcessingException {
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

		return setBasket(books);
	}
}
