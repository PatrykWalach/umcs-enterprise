package com.umcs.enterprise.node;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.node.GlobalId;
import com.umcs.enterprise.node.Node;
import graphql.schema.DataFetchingEnvironment;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import lombok.NonNull;

@DgsComponent
public class NodeDataFetcher {

	@NonNull
	private final Map<String, Function<Long, Optional<? extends Node>>> loaders = new HashMap<>();

	public NodeDataFetcher(BookRepository repository) {
		loaders.put("Book", repository::findById);
	}

	@DgsData(parentType = "Node")
	public String id(DataFetchingEnvironment env) {
		return env.<Node>getSource().getId();
	}

	@DgsQuery
	public Node node(@InputArgument String id) {
		GlobalId globalId = GlobalId.from(id);
		var loader = loaders.get(globalId.className());

		if (loader == null) {
			return null;
		}

		return loader.apply(globalId.databaseId()).orElse(null);
	}
}
