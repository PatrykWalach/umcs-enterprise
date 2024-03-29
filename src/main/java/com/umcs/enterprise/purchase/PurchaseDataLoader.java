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
public class PurchaseDataLoader implements MappedBatchLoader<Long, Purchase> {

	@NonNull
	private final PurchaseService repository;

	@NonNull
	private final DelegatingSecurityContextAsyncTaskExecutor executor;

	@Override
	public CompletionStage<Map<Long, Purchase>> load(Set<Long> keys) {
		return CompletableFuture.supplyAsync(
			() ->
				repository
					.findAllById(keys)
					.stream()
					.collect(Collectors.toMap(Node<Long>::getDatabaseId, Function.identity())),
			executor
		);
	}
}
