package com.umcs.enterprise.basket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

		GlobalId<LinkedHashMap<String, Integer>> globalId = GlobalId.from(id);

		assert Objects.equals(globalId.className(), "Basket");

		Map<Book, Integer> books = bookRepository
			.findAllById(globalId.databaseId().keySet().stream().map(UUID::fromString).toList())
			.stream()
			.collect(
				Collectors.toMap(
					Function.identity(),
					book -> globalId.databaseId().get(book.getDatabaseId().toString())
				)
			);

		return new Basket(books);
	}
}
