package com.umcs.enterprise;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.umcs.enterprise.basket.Basket;
import com.umcs.enterprise.basket.BookEdge;
import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.types.BasketBookInput;
import com.umcs.enterprise.types.WhereUniqueBasketInput;
import com.umcs.enterprise.types.WhereUniqueBookInput;
import com.umcs.enterprise.user.User;
import com.umcs.enterprise.user.UserService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(classes = EnterpriseApplication.class)
@ExtendWith(CleanDb.class)
class BasketDataFetcherTest {

	@Autowired
	WebApplicationContext context;

	@BeforeEach
	public void beforeEach() {
		WebTestClient client = MockMvcWebTestClient
			.bindToApplicationContext(context)
			.configureClient()
			.baseUrl("/graphql")
			.build();

		graphQlTester = HttpGraphQlTester.create(client);
	}

	private HttpGraphQlTester graphQlTester;

	@Autowired
	private UserService userService;

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
	@WithMockUser(username = "user")
	void basket_user() throws JsonProcessingException {
		//        given

		var user = userService.save(
			User.newBuilder().authorities(Collections.singletonList("USER")).username("user").build()
		);

		List<Book> books = bookRepository.saveAll(
			List.of(
				Book.newBuilder().price((2L)).title("Title 1").build(),
				Book.newBuilder().price((3L)).title("Title 2").build()
			)
		);

		Basket basket = new Basket();

		books.forEach(basket::add);

		this.graphQlTester.documentName("BasketDataFetcherTest_basket")
			.variable("id", basket.getId())
			//        when
			.execute()
			//                then
			.errors()
			.verify()
			.path("basket.books.edges[*].node.title")
			.entity(List.class)
			.isEqualTo(basket.getBooks().stream().map(BookEdge::getBook).map(Book::getTitle).toList())
			.path("basket.books.edges[*].quantity")
			.entity(List.class)
			.isEqualTo(basket.getBooks().stream().map(BookEdge::getQuantity).toList());
	}

	@Autowired
	private BookRepository bookRepository;

	@Test
	void basketBook_anonymous() throws JsonProcessingException {
		//        given

		var book = bookRepository.save(Book.newBuilder().price((10L)).build());

		String id = new Basket().getId();

		for (int i = 1; i < 3; i++) {
			id =
				this.graphQlTester.documentName("BasketDataFetcherTest_basketBook")
					.variable(
						"input",
						BasketBookInput
							.newBuilder()
							.basket(new WhereUniqueBasketInput(id))
							.book(WhereUniqueBookInput.newBuilder().id(book.getId()).build())
							.build()
					)
					//        when
					.execute()
					//                then
					.errors()
					.verify()
					.path("basketBook.edge.node.id")
					.entity(String.class)
					.isEqualTo((book.getId()))
					.path("basketBook.edge.quantity")
					.entity(Integer.class)
					.isEqualTo((i))
					.path("basketBook.basket.id")
					.entity(String.class)
					.get();
		}
	}

	@Test
	@WithMockUser(username = "user")
	void basketBook_user() throws JsonProcessingException {
		//        given

		var user = userService.save(
			User.newBuilder().authorities(Collections.singletonList("USER")).username("user").build()
		);

		var book = bookRepository.save(Book.newBuilder().price((10L)).build());

		String id = new Basket().getId();

		for (int i = 1; i < 3; i++) {
			id =
				this.graphQlTester.documentName("BasketDataFetcherTest_basketBook")
					.variable(
						"input",
						BasketBookInput
							.newBuilder()
							.basket(new WhereUniqueBasketInput(id))
							.book(WhereUniqueBookInput.newBuilder().id(book.getId()).build())
							.build()
					)
					//        when
					.execute()
					//                then
					.errors()
					.verify()
					.path("basketBook.edge.node.id")
					.entity(String.class)
					.isEqualTo((book.getId()))
					.path("basketBook.edge.quantity")
					.entity(Integer.class)
					.isEqualTo((i))
					.path("basketBook.basket.id")
					.entity(String.class)
					.get();
		}
	}
}
