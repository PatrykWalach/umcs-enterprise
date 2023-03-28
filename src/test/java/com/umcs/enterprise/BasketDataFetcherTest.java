package com.umcs.enterprise;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.types.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.WebGraphQlTester;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = EnterpriseApplication.class)
class BasketDataFetcherTest {

	@Autowired
	private WebGraphQlTester graphQlTester;

	@Test
	void basket() {
		//        given
		this.graphQlTester.documentName("BasketDataFetcherTest_basket")
			//        when
			.execute()
			//                then
			.errors()
			.verify()
			.path("basket.id")
			.entity(String.class)
			.matches(s -> !s.isBlank())
			.path("basket.books.edges")
			.entity(List.class)
			.isEqualTo(Collections.emptyList());
	}

	@Autowired
	private BookRepository bookRepository;

	@Test
	void basketBook() {
		//        given
		var id =
			this.graphQlTester.documentName("BasketDataFetcherTest_basket")
				.execute()
				.path("basket.id")
				.entity(String.class)
				.get();

		var book = bookRepository.save(Book.newBuilder().build());

		IntStream
			.range(1, 3)
			.forEachOrdered(i -> {
				this.graphQlTester.documentName("BasketDataFetcherTest_basketBook")
					.variable(
						"input",
						BasketBookInput
							.newBuilder()
							.basket(WhereUniqueBasketInput.newBuilder().id(id).build())
							.book(WhereUniqueBookInput.newBuilder().id(book.getId()).build())
							.build()
					)
					//        when
					.execute()
					//                then
					.errors()
					.verify()
					.path("basketBook.basket.id")
					.entity(String.class)
					.matches(s -> !s.isBlank())
					.path("basketBook.basket.books.edges[*].node.id")
					.entity(List.class)
					.isEqualTo(Collections.singletonList(book.getId()))
					.path("basketBook.basket.books.edges[*].quantity")
					.entity(List.class)
					.isEqualTo(Collections.singletonList(i));
			});
	}

	@Test
	void unbasketBook() {
		this.graphQlTester.documentName("BasketDataFetcherTest_basket")
			//        when
			.execute()
			//                then
			.errors()
			.verify()
			.path("basket.id")
			.entity(String.class)
			.matches(s -> !s.isBlank())
			.path("basket.books.edges")
			.entity(List.class)
			.isEqualTo(Collections.emptyList());
	}
}
