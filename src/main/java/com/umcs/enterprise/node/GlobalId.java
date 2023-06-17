package com.umcs.enterprise.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record GlobalId<T>(@JsonProperty("t") String className, @JsonProperty("i") T databaseId) {
	public String encode() throws JsonProcessingException {
		return java.util.Base64
			.getEncoder()
			.encodeToString(new ObjectMapper().writeValueAsString(this).getBytes(StandardCharsets.UTF_8));
	}

	public static <U> GlobalId<U> from(String c, TypeReference<GlobalId<U>> typeReference)
		throws JsonProcessingException {
		return new ObjectMapper()
			.readValue(
				new String(java.util.Base64.getDecoder().decode(c), StandardCharsets.UTF_8),
				typeReference
			);
	}
}
