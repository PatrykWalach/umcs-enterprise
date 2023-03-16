package com.umcs.enterprise;

import graphql.GraphQLException;
import org.dataloader.DataLoader;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Controller
public class NodeController {

    private final BookRepository repository;
    private final Map<String, DataLoader<Long, ? extends Node>> loaders = new HashMap<>();

    public NodeController(BookRepository repository) {
        this.repository = repository;

    }

    @SchemaMapping(typeName = "Book")
    public String id(Node node) {
        return node.getId();
    }

    @QueryMapping
    public CompletableFuture<Node> node(@Argument String id, DataLoader<Long, Book> books) {
        loaders.put("Book", books);

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

        return loader.load(databaseId).thenApply(Function.identity());
    }
}
