package com.umcs.enterprise.node;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record GlobalId(String className, UUID databaseId) {
	public String encode() {
		return java.util.Base64
			.getEncoder()
			.encodeToString((className + ":" + databaseId).getBytes(StandardCharsets.UTF_8));
	}

	public static GlobalId from(String c) {
		var id = new String(java.util.Base64.getDecoder().decode(c), StandardCharsets.UTF_8);

		String[] split = id.split(":");

		if (split.length != 2) {
			throw new IllegalArgumentException("Invalid id");
		}

		String className = split[0];
		return new GlobalId(className,  UUID.fromString(split[1]));
	}
}
