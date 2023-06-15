package com.umcs.enterprise;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.umcs.enterprise.types.CreateUserInput;
import com.umcs.enterprise.types.RegisterInput;
import com.umcs.enterprise.user.User;
import com.umcs.enterprise.user.UserService;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.function.Predicate;
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
class UserDataFetcherTest {

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
	private UserService userRepository;

	@Test
	void register_taken() {
		//        given

		var input = new RegisterInput(new CreateUserInput("user", "user"));
		userRepository.save(User.newBuilder().username("user").build());

		this.graphQlTester.documentName("UserDataFetcherTest_register")
			.variable("input", input)
			//                when
			.execute()
			//                then
			.errors()
			.verify()
			.path("register.name")
			.entity(String.class)
			.isEqualTo("You are not original enough");
		//		Assertions.assertEquals(1L, userRepository.count());
	}

	@Test
	void register() {
		//        given

		var input = new RegisterInput(new CreateUserInput("user", "user"));

		this.graphQlTester.documentName("UserDataFetcherTest_register")
			.variable("input", input)
			//                when
			.execute()
			//                then
			.errors()
			.verify()
			.path("register.user.name")
			.entity(String.class)
			.isEqualTo(input.getUser().getName());
		//		Assertions.assertEquals(1L, userRepository.count());
	}

	@Test
	@WithMockUser(username = "user")
	void viewer() {
		//        given
		var user = userRepository.save(
			User
				.newBuilder()
				.authorities(Collections.singletonList(("USER")))
				.password("user")
				.username("user")
				.build()
		);

		this.graphQlTester.documentName("UserDataFetcherTest_viewer")
			//                when
			.execute()
			//                then
			.errors()
			.verify()
			.path("viewer.name")
			.entity(String.class)
			.isEqualTo(user.getUsername());
	}

	@Test
	void viewer_anonymous() {
		//        given

		this.graphQlTester.documentName("UserDataFetcherTest_viewer")
			//                when
			.execute()
			//                then
			.errors()
			.verify()
			.path("viewer")
			.valueIsNull();
	}
}
