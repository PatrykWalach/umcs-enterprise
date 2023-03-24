package com.umcs.enterprise;

public interface Node {
	Long getDatabaseId();

	default String getId() {
		return new GlobalId(getClass().getSimpleName(), getDatabaseId()).encode();
	}
}
