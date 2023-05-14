package com.umcs.enterprise.basket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import com.netflix.graphql.dgs.exceptions.DgsInvalidInputArgumentException;
import com.umcs.enterprise.auth.JwtService;
import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.user.User;
import com.umcs.enterprise.user.UserRepository;
import io.jsonwebtoken.Claims;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
public class UserBasketService implements BasketService {

	@NonNull
	private final BasketRepository basketRepository;

	@NonNull
	private final BookEdgeRepository bookEdgeRepository;

	@Override
	public @NonNull Basket getBasket() throws JsonProcessingException {
		return basketRepository.findByUser_Username(
			SecurityContextHolder.getContext().getAuthentication().getName()
		);
	}

	@NonNull
	private final BookRepository bookRepository;

	@Override
	public @NonNull Basket basketBook(Basket basket, @NonNull UUID databaseId)
		throws JsonProcessingException {
		BookEdge bookEdge = bookEdgeRepository.findByBasket_User_UsernameAndBook_DatabaseId(
			SecurityContextHolder.getContext().getAuthentication().getName(),
			databaseId
		);

		Basket basket = getBasket();

		if (bookEdge == null) {
			bookEdge =
				BookEdge
					.newBuilder()
					.quantity(0)
					.book(bookRepository.findById(databaseId).orElseThrow(DgsEntityNotFoundException::new))
					.basket(basket)
					.build();
		}

		bookEdge.setQuantity(bookEdge.getQuantity() + 1);
		bookEdgeRepository.save(bookEdge);

		return bookEdge;
	}

	@Override
	public @NonNull BookEdge unbasketBook(@NonNull UUID databaseId) throws JsonProcessingException {
		Basket basket = getBasket();

		BookEdge bookEdge = bookEdgeRepository.findByBasket_User_UsernameAndBook_DatabaseId(
			SecurityContextHolder.getContext().getAuthentication().getName(),
			databaseId
		);

		if (bookEdge == null) {
			return basket.getId();
		}

		if (bookEdge.getQuantity() < 2) {
			bookEdgeRepository.delete(bookEdge);
			return basket.getId();
		}

		bookEdge.setQuantity(bookEdge.getQuantity() - 1);
		bookEdgeRepository.save(bookEdge);

		return basket.getId();
	}
}
