package com.umcs.enterprise;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public interface Node {
	Long getDatabaseId();

	default String getId() {
		return new GlobalId(getClass().getSimpleName(), getDatabaseId()).encode();
	}
}
