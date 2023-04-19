package com.umcs.enterprise.user;

import com.netflix.graphql.dgs.*;
import com.umcs.enterprise.auth.JwtService;
import com.umcs.enterprise.types.*;
import graphql.schema.DataFetchingEnvironment;
import io.jsonwebtoken.Jwts;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

@DgsComponent
@RequiredArgsConstructor
public class UserDataFetcher {

	@NonNull
	private final AuthenticationManager authenticationManager;

	@DgsMutation
	public LoginResult login(@InputArgument LoginInput input) {
		try {
			Authentication auth = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(input.getUsername(), input.getPassword())
			);

			String token = jwtService.signToken(
				Jwts.builder().setClaims(new HashMap<>()).setSubject(auth.getName())
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

	@DgsMutation
	public RegisterResult register(@InputArgument RegisterInput input) {
		try {
			User user = userRepository.save(
				User
					.newBuilder()
						.databaseId(UUID.fromString(input.getDatabaseId()))
					.authorities(Collections.singletonList("USER"))
					.username(input.getUsername())
					.password(input.getPassword())
					.build()
			);

			String token = jwtService.signToken(
				Jwts.builder().setClaims(new HashMap<>()).setSubject(user.getUsername())
			);

			return RegisterSuccess.newBuilder().token(token).build();
		} catch (DataIntegrityViolationException e) {
			return RegisterError.newBuilder().username("You are not original enough").build();
		}
	}
}
