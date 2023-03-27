package com.umcs.enterprise.node;

import com.umcs.enterprise.node.GlobalId;

public interface Node {
	Long getDatabaseId();

	default String getId() {
		return new GlobalId(getClass().getSimpleName(), getDatabaseId()).encode();
	}
}
