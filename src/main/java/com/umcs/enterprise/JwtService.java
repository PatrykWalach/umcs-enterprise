package com.umcs.enterprise;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

	@Value("${spring.security.authentication.jwt.secret}")
	private String secret;

	public String signToken(JwtBuilder builder) {
		return builder
			.setIssuedAt(new Date())
			.setExpiration(Date.from(Instant.now().plusSeconds(60 * 24)))
			.signWith(getKey(), SignatureAlgorithm.HS512)
			.compact();
	}

	public SecretKey getKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}
}
