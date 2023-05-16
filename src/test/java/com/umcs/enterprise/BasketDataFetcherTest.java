package com.umcs.enterprise;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.umcs.enterprise.auth.JwtService;
import com.umcs.enterprise.basket.BasketRepository;
import com.umcs.enterprise.basket.BookEdge;
import com.umcs.enterprise.basket.BookEdgeRepository;
import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.cover.Cover;
import com.umcs.enterprise.cover.CoverRepository;
import com.umcs.enterprise.types.*;
import com.umcs.enterprise.user.User;
import com.umcs.enterprise.user.UserService;
import io.jsonwebtoken.Jwts;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = EnterpriseApplication.class)
@ExtendWith(CleanDb.class)
class BasketDataFetcherTest {

	@Autowired
	private HttpGraphQlTester graphQlTester;

	@Autowired
	private UserService userService;

	@Autowired
	private JwtService jwtService;

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

	@Test
	void basket_user() {
		//        given

		var user = userService.save(
			User.newBuilder().authorities(Collections.singletonList("USER")).username("user").build()
		);

		List<Book> books = bookRepository.saveAll(
			List.of(
				Book
					.newBuilder()
					.price(BigDecimal.valueOf(2))
					.cover(coverRepository.save(Cover.newBuilder().build()))
					.title("Title 1")
					.build(),
				Book
					.newBuilder()
					.price(BigDecimal.valueOf(3))
					.cover(coverRepository.save(Cover.newBuilder().build()))
					.title("Title 2")
					.build()
			)
		);

		List<BookEdge> edges = bookEdgeRepository.saveAll(
			books
				.stream()
				.map(book -> BookEdge.newBuilder().book(book).basket(user.getBasket()).quantity(1).build())
				.toList()
		);

		String token = jwtService.signToken(
			Jwts
				.builder()
				.setExpiration(Date.from(Instant.now().plusSeconds(60 * 24)))
				.setSubject(user.getUsername())
		);

		this.graphQlTester.mutate()
			.header("Authorization", "Bearer " + token)
			.build()
			.documentName("BasketDataFetcherTest_basket")
			//        when
			.execute()
			//                then
			.errors()
			.verify()
			.path("basket.books.edges[*].node.title")
			.entity(List.class)
			.isEqualTo(books.stream().map(Book::getTitle).toList())
			.path("basket.books.edges[*].quantity")
			.entity(List.class)
			.isEqualTo(edges.stream().map(BookEdge::getQuantity).toList());
	}

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private CoverRepository coverRepository;

	@Autowired
	private BasketRepository basketRepository;

	@Autowired
	private BookEdgeRepository bookEdgeRepository;

	@Test
	void basketBook_anonymous() {
		//        given

		String token = null;

		var book = bookRepository.save(
			Book
				.newBuilder()
				.cover(coverRepository.save(new Cover()))
				.price(BigDecimal.valueOf(10))
				.build()
		);

		var tester = this.graphQlTester;

		for (int i = 1; i < 3; i++) {
			if (token != null) {
				tester = this.graphQlTester.mutate().header("Authorization", "Bearer " + token).build();
			}

			token =
				(
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
						.path("basketBook.token.value")
						.entity(String.class)
						.get()
				);
		}
	}

	@Test
	void basketBook_user() {
		//        given

		String token = null;

		var user = userService.save(
			User.newBuilder().authorities(Collections.singletonList("USER")).username("user").build()
		);

		token =
			jwtService.signToken(
				Jwts
					.builder()
					.setExpiration(Date.from(Instant.now().plusSeconds(60 * 24)))
					.setSubject(user.getUsername())
			);

		var book = bookRepository.save(
			Book
				.newBuilder()
				.cover(coverRepository.save(new Cover()))
				.price(BigDecimal.valueOf(10))
				.build()
		);

		var tester = this.graphQlTester;

		for (int i = 1; i < 3; i++) {
			if (token != null) {
				tester = this.graphQlTester.mutate().header("Authorization", "Bearer " + token).build();
			}

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
				.isEqualTo(Collections.singletonList(i));
		}
	}
}
