package com.umcs.enterprise.user;

import com.netflix.graphql.dgs.*;
import com.umcs.enterprise.basket.*;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.types.*;
import graphql.schema.DataFetchingEnvironment;
import java.util.Collections;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@DgsComponent
@RequiredArgsConstructor
public class UserDataFetcher {

	@DgsQuery
	public Optional<User> viewer() {
		return Optional
			.ofNullable(SecurityContextHolder.getContext().getAuthentication())
			.map(Authentication::getName)
			.flatMap(userRepository::findByUsername);
	}

	@DgsData(parentType = "User")
	public String name(DataFetchingEnvironment env) {
		return env.<User>getSource().getUsername();
	}

	@NonNull
	private final UserService userRepository;

	@NonNull
	private final BookRepository bookRepository;

	@DgsMutation
	public RegisterResult register(@InputArgument RegisterInput input) {
		try {
			User user = userRepository.save(
				User
					.newBuilder()
					.authorities(Collections.singletonList("USER"))
					.username(input.getUser().getName())
					.password(input.getUser().getPassword())
					.build()
			);

			return RegisterSuccess.newBuilder().user(user).build();
		} catch (DataIntegrityViolationException e) {
			return RegisterError.newBuilder().name("You are not original enough").build();
		}
	}
}
