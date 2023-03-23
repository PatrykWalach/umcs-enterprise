package com.umcs.enterprise;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	@Value("${spring.security.authentication.jwt.secret}")
	private String secret;

	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain chain
	) throws ServletException, IOException {
		String header = request.getHeader("Authorization");
		if (header == null) {
			chain.doFilter(request, response);
			return;
		}
		String token = header.replaceFirst("Bearer ", "");

		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

		final JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();

		UserDetails user = userDetailsService.loadUserByUsername(
			parser.parseClaimsJws(token).getBody().getSubject()
		);

		UsernamePasswordAuthenticationToken state = new UsernamePasswordAuthenticationToken(
			user,
			null,
			user.getAuthorities()
		);
		state.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(state);

		chain.doFilter(request, response);
	}
}
