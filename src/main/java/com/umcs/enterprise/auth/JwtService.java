package com.umcs.enterprise.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

	public Claims parseAuthorizationHeader(String authorization) {
		return null;
	}

	public String signToken(JwtBuilder builder) {
		return null;
	}
}
