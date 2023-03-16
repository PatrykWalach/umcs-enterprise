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


        String[] split = id.split(":");
        if (split.length < 2) {
            throw new GraphQLException("Invalid id");
        }
        String className = split[0];
        Long databaseId = Long.parseLong(split[1]);
        var loader = loaders.get(className);


        if (loader == null) {
            return null;
        }

        return loader.apply(databaseId).orElse(null);
    }
}