package com.umcs.enterprise;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.umcs.enterprise.auth.JwtService;
import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.cover.Cover;
import com.umcs.enterprise.cover.CoverRepository;
import com.umcs.enterprise.purchase.BookPurchaseRepository;
import com.umcs.enterprise.purchase.Purchase;
import com.umcs.enterprise.purchase.PurchaseService;
import com.umcs.enterprise.user.User;
import com.umcs.enterprise.user.UserRepository;
import com.umcs.enterprise.user.UserService;
import io.jsonwebtoken.Jwts;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = EnterpriseApplication.class)
@ExtendWith(CleanDb.class)
class NodeDataFetcherTest {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private HttpGraphQlTester graphQlTester;

	@Autowired
	private CoverRepository coverRepository;

	@Autowired
	private PurchaseService purchaseRepository;

	@Test
	void returnsBook() {
		//        given
		var cover = coverRepository.save(new Cover());
		var book = bookRepository.save(Book.newBuilder().cover(cover).title("Book title").build());

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

	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserRepository userRepository;

	@Test
	@WithMockUser(username = "user")
	void returnsViewer() {
		//        given
		var user = userService.save(
				User.newBuilder().authorities(Collections.singletonList("USER")).username("user").build()
		);


		this.graphQlTester
			.documentName("NodeControllerTest_returnsNode")
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
	@WithMockUser
	void doesntReturnOtherUser() {
		//        given

		var other = userService.save(
			User.newBuilder().authorities(Collections.singletonList("USER")).username("other").build()
		);


		this.graphQlTester
			.documentName("NodeControllerTest_returnsNode")
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
	void returnsPurchase() {
		//        given
		var user = userService.save(
			User.newBuilder().authorities(Collections.singletonList("USER")).username("user").build()
		);
		Purchase purchase = purchaseRepository.save(Purchase.newBuilder().user(user).build());


		this.graphQlTester
			.documentName("NodeControllerTest_returnsNode")
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
	@WithMockUser("user")
	void doesntReturnOtherUserPurchase() {
		//        given

		var other = userService.save(
			User.newBuilder().authorities(Collections.singletonList("USER")).username("other").build()
		);
		Purchase purchase = purchaseRepository.save(Purchase.newBuilder().user(other).build());



		this.graphQlTester
			.documentName("NodeControllerTest_returnsNode")
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
	void notFound() {
		//        given
		var book = bookRepository.save(
			Book.newBuilder().cover(coverRepository.save(new Cover())).title("Book title").build()
		);
		book.setDatabaseId(UUID.randomUUID());

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
