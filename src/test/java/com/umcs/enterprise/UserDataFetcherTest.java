package com.umcs.enterprise;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.umcs.enterprise.auth.JwtService;
import com.umcs.enterprise.types.LoginInput;
import com.umcs.enterprise.types.RegisterInput;
import com.umcs.enterprise.user.User;
import com.umcs.enterprise.user.UserService;
import io.jsonwebtoken.Jwts;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = EnterpriseApplication.class)
@ExtendWith(CleanDb.class)
class UserDataFetcherTest {

	@Autowired
	private HttpGraphQlTester graphQlTester;

	@Autowired
	private UserService userRepository;

	@Autowired
	private JwtService jwtService;

	@Test
	void login() {
		//        given
		var input = LoginInput.newBuilder().password("user").username("user").build();
		userRepository.save(
			User
				.newBuilder()
				.username("user")
				.authorities(Collections.singletonList("USER"))
				.password("user")
				.build()
		);

		this.graphQlTester.documentName("UserDataFetcherTest_login")
			.variable("input", input)
			//                when
			.execute()
			//                then
			.errors()
			.verify()
			.path("login.token.value")
			.entity(String.class)
			.matches(Predicate.not(String::isBlank));
	}

	@Test
	void login_invalid() {
		//        given
		var input = LoginInput.newBuilder().password("user").username("user").build();
		userRepository.save(User.newBuilder().username("user").password("password").build());

		this.graphQlTester.mutate()
			.header("Accept-Language", "en")
			.build()
			.documentName("UserDataFetcherTest_login")
			.variable("input", input)
			//                when
			.execute()
			//                then
			.errors()
			.verify()
			.path("login.username")
			.entity(String.class)
			.isEqualTo("Bad credentials");
	}

	@Test
	void register_taken() {
		//        given

		var input = RegisterInput.newBuilder().password("user").username("user").build();
		userRepository.save(User.newBuilder().username("user").build());

		this.graphQlTester.documentName("UserDataFetcherTest_register")
			.variable("input", input)
			//                when
			.execute()
			//                then
			.errors()
			.verify()
			.path("register.username")
			.entity(String.class)
			.isEqualTo("You are not original enough");
		//		Assertions.assertEquals(1L, userRepository.count());
	}

	@Test
	void register() {
		//        given
		var input = RegisterInput.newBuilder().password("user").username("user").build();

		this.graphQlTester.documentName("UserDataFetcherTest_register")
			.variable("input", input)
			//                when
			.execute()
			//                then
			.errors()
			.verify()
			.path("register.token.value")
			.entity(String.class)
			.matches(Predicate.not(String::isBlank));
		//		Assertions.assertEquals(1L, userRepository.count());
	}

	@Test
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
		String token = jwtService.signToken(
			Jwts
				.builder()
				.setExpiration(Date.from(Instant.now().plusSeconds(60 * 24)))
				.setSubject(user.getUsername())
		);

		this.graphQlTester.mutate()
			.header("Authorization", "Bearer " + token)
			.build()
			.documentName("UserDataFetcherTest_viewer")
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
