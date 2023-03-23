package com.umcs.enterprise;

import com.netflix.graphql.dgs.*;
import com.umcs.enterprise.types.LoginInput;
import com.umcs.enterprise.types.LoginResult;
import com.umcs.enterprise.types.RegisterInput;
import com.umcs.enterprise.types.RegisterResult;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Collections;
import java.util.HashMap;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@DgsComponent
@RequiredArgsConstructor
public class UserDataFetcher {

	@Value("${spring.security.authentication.jwt.secret}")
	private String secret;

	private final AuthenticationManager authenticationManager;

	@DgsMutation
	public LoginResult login(@InputArgument LoginInput input) {
		Authentication auth = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(input.getUsername(), input.getPassword())
		);

		String token = jwtService.signToken(
			Jwts.builder().setClaims(new HashMap<>()).setSubject(auth.getName())
		);

		return LoginResult.newBuilder().token(token).build();
	}

	@DgsQuery
	public User viewer() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	@DgsData(parentType = "User")
	public String name(DataFetchingEnvironment env) {
		return env.<User>getSource().getUsername();
	}

	private final UserRepository userRepository;

	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;

	@DgsMutation
	public RegisterResult register(@InputArgument RegisterInput input) {
		User user = userRepository.save(
			User
				.newBuilder()
				.authorities(Collections.singletonList(("USER")))
				.username(input.getUsername())
				.password(passwordEncoder.encode(input.getPassword()))
				.build()
		);

		String token = jwtService.signToken(
			Jwts.builder().setClaims(new HashMap<>()).setSubject(user.getUsername())
		);

		return RegisterResult.newBuilder().token(token).build();
	}
}
