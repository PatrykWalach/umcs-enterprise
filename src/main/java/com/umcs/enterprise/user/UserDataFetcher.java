package com.umcs.enterprise.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.netflix.graphql.dgs.*;
import com.umcs.enterprise.auth.JwtService;
import com.umcs.enterprise.basket.*;
import com.umcs.enterprise.basket.Basket;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.types.*;
import graphql.schema.DataFetchingEnvironment;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.endpoint.DefaultPasswordTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2PasswordGrantRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.RequestHeader;

@DgsComponent
@RequiredArgsConstructor
public class UserDataFetcher {

	@DgsQuery
	public Optional<User> viewer() {
		return userRepository.findByUsername(
			SecurityContextHolder.getContext().getAuthentication().getName()
		);
	}

	@DgsData(parentType = "User")
	public String name(DataFetchingEnvironment env) {
		return env.<User>getSource().getUsername();
	}

	@NonNull
	private final UserService userRepository;

	@NonNull
	private final JwtDecoder jwtDecoder;

	@NonNull
	private final BasketRepository basketRepository;

	@NonNull
	private final BookRepository bookRepository;

	@NonNull
	private final BookEdgeRepository bookEdgeRepository;

	@DgsMutation
	public RegisterResult register(
		@InputArgument RegisterInput input,
		@RequestHeader(required = false) String Authorization
	) throws JsonProcessingException {
		try {
			User user = userRepository.save(
				User
					.newBuilder()
					.authorities(Collections.singletonList("USER"))
					.username(input.getUsername())
					.password(input.getPassword())
					.build()
			);

			Basket basket = new AnonymousBasketService(jwtDecoder, Authorization, bookRepository)
				.getBasket();

			basket.getBooks().forEach(edge -> edge.setBasket(user.getBasket()));
			bookEdgeRepository.saveAll(basket.getBooks());

			return RegisterSuccess.newBuilder().build();
		} catch (DataIntegrityViolationException e) {
			return RegisterError.newBuilder().username("You are not original enough").build();
		}
	}
}
