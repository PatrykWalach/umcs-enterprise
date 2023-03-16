package com.umcs.enterprise;

public interface Node {
	Long getDatabaseId();

	default String getId() {
		return getClass().getSimpleName() + ":" + getDatabaseId();
	}
}
