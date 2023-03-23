package com.umcs.enterprise;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.umcs.enterprise.types.CreateBookInput;
import com.umcs.enterprise.types.LoginInput;
import com.umcs.enterprise.types.RegisterInput;
import io.jsonwebtoken.Jwts;
import java.util.Collections;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.WebGraphQlTester;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = EnterpriseApplication.class)
class UserDataFetcherTest {

	@Test
	void login() {
		//        given
		var input = LoginInput.newBuilder().password("user").username("user").build();
		userRepository.save(
			User
				.newBuilder()
				.username("user")
				.authorities(Collections.singletonList("USER"))
				.password(passwordEncoder.encode("user"))
				.build()
		);

		this.graphQlTester.documentName("UserDataFetcherTest_login")
			.variable("input", input)
			//                when
			.execute()
			//                then
			.errors()
			.verify()
			.path("login.token")
			.entity(String.class)
			.isNotEqualTo("");
	}

	@Test
	void login_invalid() {
		//        given
		var input = LoginInput.newBuilder().password("user").username("user").build();
		userRepository.save(
			User.newBuilder().username("user").password(passwordEncoder.encode("password")).build()
		);

		this.graphQlTester.documentName("UserDataFetcherTest_login")
			.variable("input", input)
			//                when
			.execute()
			//                then
			.errors()
			.expect(e -> true)
			.verify()
			.path("login")
			.valueIsNull();
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
			.expect(e -> true)
			.verify()
			.path("register")
			.valueIsNull();
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
			.path("register.token")
			.entity(String.class)
			.isNotEqualTo("");
	}

	@Autowired
	private WebGraphQlTester graphQlTester;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;

	@BeforeEach
	void beforeEach() {
		userRepository.deleteAll();
	}

	@Test
	void viewer() {
		//        given
		var user = userRepository.save(
			User
				.newBuilder()
				.authorities(Collections.singletonList(("USER")))
				.password(passwordEncoder.encode("user"))
				.username("user")
				.build()
		);
		String token = jwtService.signToken(
			Jwts.builder().setClaims(new HashMap<>()).setSubject(user.getUsername())
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
			.expect(e -> true)
			.verify()
			.path("viewer")
			.valueIsNull();
	}
}
