package com.umcs.enterprise.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.netflix.graphql.dgs.*;
import com.umcs.enterprise.auth.JwtService;
import com.umcs.enterprise.basket.BasketService;
import com.umcs.enterprise.types.*;
import graphql.schema.DataFetchingEnvironment;
import io.jsonwebtoken.Jwts;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestHeader;

@DgsComponent
@RequiredArgsConstructor
public class UserDataFetcher {

	@NonNull
	private final AuthenticationManager authenticationManager;

	@NonNull
	private final BasketService basketService;

	@DgsMutation
	public LoginResult login(
		@InputArgument LoginInput input,
		@RequestHeader(required = false) String Authorization
	) throws JsonProcessingException {
		try {
			Authentication auth = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(input.getUsername(), input.getPassword())
			);

			String token = basketService.setBasket(
				jwtService.signToken(
					Jwts
						.builder()
						.setExpiration(Date.from(Instant.now().plusSeconds(60 * 24)))
						.setSubject(auth.getName())
				),
				basketService.getBasket(Authorization)
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

	@DgsTypeResolver(name = "Viewer")
	public String resolveViewer(User user) {
		if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
			return "Admin";
		}

		return "User";
	}

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

			String token = basketService.setBasket(
				jwtService.signToken(
					Jwts
						.builder()
						.setExpiration(Date.from(Instant.now().plusSeconds(60 * 24)))
						.setSubject(user.getUsername())
				),
				basketService.getBasket(Authorization)
			);

			return RegisterSuccess.newBuilder().token(token).build();
		} catch (DataIntegrityViolationException e) {
			return RegisterError.newBuilder().username("You are not original enough").build();
		}
	}
}
