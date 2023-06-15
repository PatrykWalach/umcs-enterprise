package com.umcs.enterprise.purchase;

import com.netflix.graphql.dgs.DgsDataLoader;
import com.umcs.enterprise.node.Node;
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

@DgsDataLoader(name = "PurchaseDataLoader")
@RequiredArgsConstructor
public class PurchaseDataLoader implements MappedBatchLoader<UUID, Purchase> {

	@NonNull
	private final PurchaseService repository;

	@NonNull
	private final DelegatingSecurityContextAsyncTaskExecutor executor;

	@Override
	public CompletionStage<Map<UUID, Purchase>> load(Set<UUID> keys) {
		return CompletableFuture.supplyAsync(
			() ->
				repository
					.findAllById(keys)
					.stream()
					.collect(Collectors.toMap(Node<UUID>::getDatabaseId, Function.identity())),
			executor
		);
	}
}
