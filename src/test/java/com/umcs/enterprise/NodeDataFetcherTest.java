package com.umcs.enterprise;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.purchase.BookPurchaseRepository;
import com.umcs.enterprise.purchase.Purchase;
import com.umcs.enterprise.purchase.PurchaseService;
import com.umcs.enterprise.user.User;
import com.umcs.enterprise.user.UserService;
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
class NodeDataFetcherTest {

	@Autowired
	private BookRepository bookRepository;

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
	private PurchaseService purchaseRepository;

	@Test
	void returnsBook() throws JsonProcessingException {
		//        given

		var book = bookRepository.save(Book.newBuilder().title("Book title").build());

		this.graphQlTester.documentName("NodeControllerTest_returnsNode")
			.variable("id", book.getId())
			//        when
			.execute()
			//                then
			.errors()
			.verify()
			.path("node.id")
			.entity(String.class)
			.isEqualTo(book.getId())
			.path("node.title")
			.entity(String.class)
			.isEqualTo(book.getTitle());
	}

	@Autowired
	private BookPurchaseRepository bookPurchaseRepository;

	@Autowired
	private UserService userService;

	@Test
	@WithMockUser(username = "user")
	void returnsViewer() throws JsonProcessingException {
		//        given
		var user = userService.save(
			User.newBuilder().username("user").build()
		);

		this.graphQlTester.documentName("NodeControllerTest_returnsNode")
			.variable("id", user.getId())
			//        when
			.execute()
			//                then
			.errors()
			.verify()
			.path("node.id")
			.entity(String.class)
			.isEqualTo(user.getId())
			.path("node.name")
			.entity(String.class)
			.isEqualTo(user.getUsername());
	}

	@Test
	@WithMockUser(username = "user")
	void doesntReturnOtherUser() throws JsonProcessingException {
		//        given
		var user = userService.save(
			User.newBuilder().username("user").build()
		);
		var other = userService.save(
			User.newBuilder().username("other").build()
		);

		this.graphQlTester.documentName("NodeControllerTest_returnsNode")
			.variable("id", other.getId())
			//        when
			.execute()
			//                then
			.errors()
			.expect(e -> "NOT_FOUND".equals(e.getExtensions().get("errorType")))
			.verify()
			.path("node")
			.valueIsNull();
	}

	@Test
	@WithMockUser(username = "user")
	void returnsPurchase() throws JsonProcessingException {
		//        given
		var user = userService.save(
			User.newBuilder().username("user").build()
		);
		Purchase purchase = purchaseRepository.save(Purchase.newBuilder().user(user).build());

		this.graphQlTester.documentName("NodeControllerTest_returnsNode")
			.variable("id", purchase.getId())
			//        when
			.execute()
			//                then
			.errors()
			.verify()
			.path("node.id")
			.entity(String.class)
			.isEqualTo(purchase.getId())
			.path("node.user.id")
			.entity(String.class)
			.isEqualTo(user.getId());
	}

	@Test
	@WithMockUser(username = "user")
	void doesntReturnOtherUserPurchase() throws JsonProcessingException {
		//        given

		var other = userService.save(
			User.newBuilder().username("other").build()
		);
		Purchase purchase = purchaseRepository.save(Purchase.newBuilder().user(other).build());


		var user = userService.save(
			User.newBuilder().username("user").build()
		);

		this.graphQlTester.documentName("NodeControllerTest_returnsNode")
			.variable("id", purchase.getId())
			//        when
			.execute()
			//                then
			.errors()
			.expect(e -> "NOT_FOUND".equals(e.getExtensions().get("errorType")))
			.verify()
			.path("node")
			.valueIsNull();
	}

	@Test
	void notFound() throws JsonProcessingException {
		//        given
		var book = bookRepository.save(Book.newBuilder().title("Book title").build());
		book.setDatabaseId(-1L);

		this.graphQlTester.documentName("NodeControllerTest_returnsNode")
			.variable("id", book.getId())
			//        when
			.execute()
			//                then
			.errors()
			.expect(e -> "NOT_FOUND".equals(e.getExtensions().get("errorType")))
			.verify()
			.path("node")
			.valueIsNull();
	}
}
