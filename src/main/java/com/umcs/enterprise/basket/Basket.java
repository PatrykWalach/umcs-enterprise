package com.umcs.enterprise.basket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.node.Node;
import java.util.*;
import java.util.stream.Collectors;

public class Basket implements Node<Map<Long, Integer>> {

	private final Map<Book, Integer> books;

	public Basket(Map<Book, Integer> books) {
		this.books = new HashMap<>(books);
		this.books.putAll(books);
	}

	public Basket() {
		this(new HashMap<>());
	}

	public List<BookEdge> getBooks() {
		return books.entrySet().stream().map(e -> new BookEdge(e.getKey(), e.getValue())).sorted(Comparator.comparing(
				(edge)-> edge.getBook().getDatabaseId()
		)).toList();
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
	public Map<Long, Integer> getDatabaseId() {
		return books
			.entrySet()
			.stream()
			.collect(Collectors.toMap(e -> e.getKey().getDatabaseId(), Map.Entry::getValue));
	}
}
