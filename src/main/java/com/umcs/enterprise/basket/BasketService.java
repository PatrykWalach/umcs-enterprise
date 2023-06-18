package com.umcs.enterprise.basket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.node.GlobalId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasketService {

	@NonNull
	private final BookRepository bookRepository;

	public Basket getBasket(String id) throws JsonProcessingException {
		if (id == null) {
			return new Basket();
		}

		GlobalId<Map<Long, Integer>> globalId = GlobalId.from(id, new TypeReference<>() {});

		assert Objects.equals(globalId.className(), "Basket");

		var books = bookRepository
			.findAllById(globalId.databaseId().keySet().stream().toList())
			.stream()
			.collect(
				Collectors.toMap(
					Function.identity(),
					book -> globalId.databaseId().get(book.getDatabaseId())
				)
			);

		return new Basket(books);
	}
}
