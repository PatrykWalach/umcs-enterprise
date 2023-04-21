package com.umcs.enterprise.basket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umcs.enterprise.auth.JwtService;
import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.book.BookRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasketService {

	@NonNull
	private final JwtService jwtService;

	public String setBasket(String Authorization, Map<UUID, Integer> basket)
		throws JsonProcessingException {
		JwtBuilder builder = setBasket(Jwts.builder(), basket);

		builder.setExpiration(Date.from(Instant.now().plusSeconds(60 * 60 * 24 * 365)));

		if (Authorization == null) {
			return jwtService.signToken(builder);
		}

		Claims jwt = jwtService.parseAuthorizationHeader(Authorization);

		if (jwt.getSubject() != null) {
			builder.setSubject(jwt.getSubject());
			builder.setExpiration(jwt.getExpiration());
		}

		return jwtService.signToken(builder);
	}

	public JwtBuilder setBasket(JwtBuilder builder, Map<UUID, Integer> basket)
		throws JsonProcessingException {
		return builder.setClaims(
			Map.of("http://localhost:8080/graphql/basket", new ObjectMapper().writeValueAsString(basket))
		);
	}

	@NonNull
	private final BookRepository bookRepository;

	public Map<UUID, Integer> getBasket(String Authorization) throws JsonProcessingException {
		if (Authorization == null) {
			return new HashMap<>();
		}

		try {
			String basket = jwtService
				.parseAuthorizationHeader(Authorization)
				.get("http://localhost:8080/graphql/basket", String.class);

			if (basket == null) {
				return new HashMap<>();
			}

			Map<UUID, Integer> parsed = new ObjectMapper().readValue(basket, new TypeReference<>() {});

			return bookRepository
				.findAllById(parsed.keySet())
				.stream()
				.map(Book::getDatabaseId)
				.collect(Collectors.toMap(Function.identity(), parsed::get));
		} catch (IllegalArgumentException e) {
			return new HashMap<>();
		}
	}
}
