package com.umcs.enterprise.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

	@Value("${spring.security.authentication.jwt.secret}")
	private String secret;

	public String signToken(JwtBuilder builder) {
		return builder
			.setIssuedAt(new Date())
			.setNotBefore(new Date())

			.signWith(getKey(), SignatureAlgorithm.HS512)
			.compact();
	}

	public SecretKey getKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}


	public Claims parseAuthorizationHeader(@NonNull  String header){
		return (parseToken(header.replaceFirst("Bearer ", "")));
	}

	private Claims parseToken(String token){


		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

		final JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();

		return parser.parseClaimsJws(token).getBody();
	}
}
