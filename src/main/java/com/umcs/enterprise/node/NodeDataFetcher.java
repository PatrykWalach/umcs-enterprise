package com.umcs.enterprise.node;

import com.netflix.graphql.dgs.*;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.node.GlobalId;
import com.umcs.enterprise.node.Node;
import graphql.schema.DataFetchingEnvironment;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import lombok.NonNull;

@DgsComponent
public class NodeDataFetcher {

	@DgsData(parentType = "Node")
	public String id(DataFetchingEnvironment env) {
		return env.<Node>getSource().getId();
	}

	@DgsQuery
	public CompletableFuture<Object> node(@InputArgument String id, DgsDataFetchingEnvironment enf) {
		GlobalId globalId = GlobalId.from(id);

		return enf.getDataLoader(globalId.className() + "DataLoader").load(globalId.databaseId());
	}
}
