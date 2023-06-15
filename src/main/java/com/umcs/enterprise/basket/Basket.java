package com.umcs.enterprise.basket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.node.Node;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class Basket implements Node {

	private final Map<Book, Integer> books;

	public Basket(Map<Book, Integer> books) {
		this.books = books;
	}

	public Basket() {
		this(new LinkedHashMap<>());
	}

	public List<BookEdge> getBooks() {
		return books.entrySet().stream().map(e -> new BookEdge(e.getKey(), e.getValue())).toList();
	}

	public BookEdge add(Book book) {
		Integer quantity = books.merge(book, 1, Integer::sum);

		return new BookEdge(book, quantity);
	}

	public BookEdge remove(Book book) {
		Integer quantity = books.computeIfPresent(
			book,
			(key, value) -> {
				if (value < 2) {
					return null;
				}
				return value - 1;
			}
		);

		if (quantity == null) {
			return null;
		}

		return new BookEdge(book, quantity);
	}

	@Override
	public Map<UUID, Integer> getDatabaseId() {
		return books
			.entrySet()
			.stream()
			.collect(Collectors.toMap(e -> e.getKey().getDatabaseId(), Map.Entry::getValue));
	}
}
