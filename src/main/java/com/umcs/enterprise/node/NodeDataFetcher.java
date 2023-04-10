package com.umcs.enterprise.node;

import com.netflix.graphql.dgs.*;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import com.umcs.enterprise.node.GlobalId;
import com.umcs.enterprise.node.Node;
import graphql.schema.DataFetchingEnvironment;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.dataloader.DataLoader;

@DgsComponent
public class NodeDataFetcher {

	@DgsData(parentType = "Node")
	public String id(DataFetchingEnvironment env) {
		return env.<Node>getSource().getId();
	}

	@DgsQuery
	public CompletableFuture<Node> node(@InputArgument String id, DgsDataFetchingEnvironment enf) {
		GlobalId globalId = GlobalId.from(id);
		DataLoader<Object, Node> loader = enf.getDataLoader(globalId.className() + "DataLoader");

		if (loader == null) {
			throw new DgsEntityNotFoundException();
		}

		return loader
			.load(globalId.databaseId())
			.thenApply(Optional::ofNullable)
			.thenApply(optional -> optional.orElseThrow(DgsEntityNotFoundException::new));
	}
}
