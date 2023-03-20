package com.umcs.enterprise;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import graphql.GraphQLException;
import graphql.schema.DataFetchingEnvironment;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@DgsComponent
public class NodeDataFetcher {

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
		var loader = loaders.get(globalId.getClassName());

		if (loader == null) {
			return null;
		}

		return loader.apply(globalId.getDatabaseId()).orElse(null);
	}
}
