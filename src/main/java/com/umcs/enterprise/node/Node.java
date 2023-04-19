package com.umcs.enterprise.node;

import com.umcs.enterprise.node.GlobalId;

import java.util.UUID;

public interface Node {
	UUID getDatabaseId();

	default String getId() {
		return new GlobalId(getClass().getSimpleName(), getDatabaseId()).encode();
	}
}
