package com.umcs.enterprise;

import java.nio.charset.StandardCharsets;

public class GlobalId {

	private final String className;
	private final Long databaseId;

	public String getClassName() {
		return className;
	}

	public Long getDatabaseId() {
		return databaseId;
	}

	public String encode() {
		return java.util.Base64
			.getEncoder()
			.encodeToString((className + ":" + databaseId).getBytes(StandardCharsets.UTF_8));
	}

	public GlobalId(String className, Long databaseId) {
		this.className = className;
		this.databaseId = databaseId;
	}

	public static GlobalId from(String c) {
		var id = new String(java.util.Base64.getDecoder().decode(c), StandardCharsets.UTF_8);

		String[] split = id.split(":");

		if (split.length != 2) {
			throw new IllegalArgumentException("Invalid id");
		}

		String className = split[0];
		Long databaseId = Long.parseLong(split[1]);

		return new GlobalId(className, databaseId);
	}
}
