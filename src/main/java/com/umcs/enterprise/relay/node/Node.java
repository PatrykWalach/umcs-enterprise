package com.umcs.enterprise.relay.node;

import java.util.UUID;

public interface Node {
	UUID getDatabaseId();

	default String getId() {
		return new GlobalId(getClass().getSimpleName(), getDatabaseId()).encode();
	}
}
