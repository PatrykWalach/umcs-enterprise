package com.umcs.enterprise.basket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import com.netflix.graphql.dgs.exceptions.DgsInvalidInputArgumentException;
import com.umcs.enterprise.auth.JwtService;
import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.types.Token;
import com.umcs.enterprise.user.User;
import com.umcs.enterprise.user.UserRepository;
import io.jsonwebtoken.Claims;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
public class UserBasketService implements BasketService {

	@NonNull
	private final JwtService jwtService;

	@NonNull
	private final BasketRepository basketRepository;

	@NonNull
	private final BookEdgeRepository bookEdgeRepository;

	@NonNull
	private final String Authorization;

	@Override
	public @NonNull Basket getBasket() throws JsonProcessingException {
		Claims token = jwtService.parseAuthorizationHeader(Authorization);
		assert token.getSubject() != null;
		return basketRepository.findByUser_Username(token.getSubject());
	}

	@NonNull
	private final BookRepository bookRepository;

	@Override
	public  Token basketBook(@NonNull UUID databaseId) throws JsonProcessingException {
		Claims token = jwtService.parseAuthorizationHeader(Authorization);
		assert token.getSubject() != null;

		BookEdge bookEdge = bookEdgeRepository.findByBasket_User_UsernameAndBook_DatabaseId(
			token.getSubject(),
			databaseId
		);

		if (bookEdge == null) {
			bookEdge =
				BookEdge
					.newBuilder()
					.quantity(0)
					.book(bookRepository.findById(databaseId).orElseThrow(DgsEntityNotFoundException::new))
					.basket(getBasket())
					.build();
		}

		bookEdge.setQuantity(bookEdge.getQuantity() + 1);
		bookEdgeRepository.save(bookEdge);

		return null;
	}

	@Override
	public   Token unbasketBook(@NonNull UUID databaseId) throws JsonProcessingException {
		Claims token = jwtService.parseAuthorizationHeader(Authorization);
		assert token.getSubject() != null;

		BookEdge bookEdge = bookEdgeRepository.findByBasket_User_UsernameAndBook_DatabaseId(
			token.getSubject(),
			databaseId
		);

		if (bookEdge == null) {
			return null;
		}

		if (bookEdge.getQuantity() < 2) {
			bookEdgeRepository.delete(bookEdge);
			return null;
		}

		bookEdge.setQuantity(bookEdge.getQuantity() - 1);
		bookEdgeRepository.save(bookEdge);

		return null;
	}
}
