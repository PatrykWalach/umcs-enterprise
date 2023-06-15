package com.umcs.enterprise.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record GlobalId<T>(String className, T databaseId) {
	public String encode() throws JsonProcessingException {
		return java.util.Base64
			.getEncoder()
			.encodeToString(new ObjectMapper().writeValueAsString(this).getBytes(StandardCharsets.UTF_8));
	}

	public static <U> GlobalId<U> from(String c) throws JsonProcessingException {
		return new ObjectMapper()
			.readValue(
				new String(java.util.Base64.getDecoder().decode(c), StandardCharsets.UTF_8),
				new TypeReference<>() {}
			);
	}
}
