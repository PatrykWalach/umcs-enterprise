package com.umcs.enterprise.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.umcs.enterprise.node.GlobalId;
import java.util.UUID;

public interface Node<T> {
	T getDatabaseId();

	default String getId() {
		try {
			return new GlobalId<>(getClass().getSimpleName(), getDatabaseId()).encode();
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
