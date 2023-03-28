package com.umcs.enterprise.book;

import com.netflix.graphql.dgs.DgsDataLoader;
import com.umcs.enterprise.node.Node;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;

@DgsDataLoader(name = "BookDataLoader")
@RequiredArgsConstructor
public class BookDataLoader implements MappedBatchLoader<Long, Book> {

	@NonNull
	private final BookRepository repository;

	@Override
	public CompletionStage<Map<Long, Book>> load(Set<Long> keys) {
		return CompletableFuture.supplyAsync(() ->
			repository
				.findAllById(keys)
				.stream()
				.collect(Collectors.toMap(Node::getDatabaseId, Function.identity()))
		);
	}
}
