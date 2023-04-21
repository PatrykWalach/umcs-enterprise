package com.umcs.enterprise;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.cover.Cover;
import com.umcs.enterprise.cover.CoverRepository;
import com.umcs.enterprise.types.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = EnterpriseApplication.class)
@ExtendWith(CleanDb.class)
class BasketDataFetcherTest {

	@Autowired
	private HttpGraphQlTester graphQlTester;

	@Test
	void basket() {
		//        given
		this.graphQlTester.documentName("BasketDataFetcherTest_basket")
			//        when
			.execute()
			//                then
			.errors()
			.verify()
			.path("basket.books.edges")
			.entity(List.class)
			.isEqualTo(Collections.emptyList());
	}

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private CoverRepository coverRepository;

	@Test
	void basketBook() {
		//        given

		var book = bookRepository.save(
			Book
				.newBuilder()
				.cover(coverRepository.save(new Cover()))
				.price(BigDecimal.valueOf(10))
				.build()
		);

		String token = null;
		var tester = this.graphQlTester;

		for (int i = 1; i < 3; i++) {
			if (token != null) {
				tester = this.graphQlTester.mutate().header("Authorization", "Bearer " + token).build();
			}

			token =
				tester
					.documentName("BasketDataFetcherTest_basketBook")
					.variable(
						"input",
						BasketBookInput
							.newBuilder()
							.book(WhereUniqueBookInput.newBuilder().id(book.getId()).build())
							.build()
					)
					//        when
					.execute()
					//                then
					.errors()
					.verify()
					.path("basketBook.basket.books.edges[*].node.id")
					.entity(List.class)
					.isEqualTo(Collections.singletonList(book.getId()))
					.path("basketBook.basket.books.edges[*].quantity")
					.entity(List.class)
					.isEqualTo(Collections.singletonList(i))
					.path("basketBook.token")
					.entity(String.class)
					.get();
		}
	}
}
