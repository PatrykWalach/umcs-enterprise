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
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestHeader;

@DgsComponent
@RequiredArgsConstructor
public class UserDataFetcher {

	@NonNull
	private final AuthenticationManager authenticationManager;



	@DgsMutation
	public LoginResult login(
		@InputArgument LoginInput input,
		@RequestHeader(required = false) String Authorization
	) throws JsonProcessingException {
		try {
			Authentication auth = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(input.getUsername(), input.getPassword())
			);



			Basket basket = new AnonymousBasketService(jwtService, Authorization, bookRepository).getBasket();

			if(basket.getBooks().size() > 0){
				Basket saved = basketRepository.findByUser_Username(auth.getName());
				basket.getBooks().forEach(edge->edge.setBasket(saved));
				bookEdgeRepository.saveAll(basket.getBooks());
			}

			String token =  (
				jwtService.signToken(
					Jwts
						.builder()
						.setExpiration(Date.from(Instant.now().plusSeconds(60 * 24)))
						.setSubject(auth.getName())
				)

			);

			return LoginSuccess.newBuilder().token(token).build();
		} catch (AuthenticationException e) {
			return LoginError.newBuilder().username(e.getLocalizedMessage()).build();
		}
	}

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
	private final JwtService jwtService;
@NonNull
private  final
	BasketRepository basketRepository;
	@NonNull private final BookRepository bookRepository;	@NonNull
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


			Basket 				basket = new AnonymousBasketService(jwtService, Authorization, bookRepository).getBasket();

			basket.getBooks().forEach(edge->edge.setBasket(user.getBasket()));
			bookEdgeRepository.saveAll(basket.getBooks());



			String token =  (
				jwtService.signToken(
					Jwts
						.builder()
						.setExpiration(Date.from(Instant.now().plusSeconds(60 * 24)))
						.setSubject(user.getUsername())
				)
			);

			return RegisterSuccess.newBuilder().token(token).build();
		} catch (DataIntegrityViolationException e) {
			return RegisterError.newBuilder().username("You are not original enough").build();
		}
	}
}
