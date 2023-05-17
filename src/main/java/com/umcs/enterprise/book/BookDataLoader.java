package com.umcs.enterprise.book;

import com.netflix.graphql.dgs.DgsDataLoader;
import com.umcs.enterprise.relay.node.Node;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

@DgsDataLoader(name = "BookDataLoader")
@RequiredArgsConstructor
public class BookDataLoader implements MappedBatchLoader<UUID, Book> {

	@NonNull
	private final BookRepository repository;

	@NonNull
	private final DelegatingSecurityContextAsyncTaskExecutor executor;

	@Override
	public CompletionStage<Map<UUID, Book>> load(Set<UUID> keys) {
		return CompletableFuture.supplyAsync(
			() ->
				repository
					.findAllById(keys)
					.stream()
					.collect(Collectors.toMap(Node::getDatabaseId, Function.identity())),
			executor
		);
	}
}
