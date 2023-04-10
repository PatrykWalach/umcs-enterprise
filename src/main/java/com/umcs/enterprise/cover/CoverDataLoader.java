package com.umcs.enterprise.cover;

import com.netflix.graphql.dgs.DgsDataLoader;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

@DgsDataLoader(name = "CoverDataLoader")
@RequiredArgsConstructor
public class CoverDataLoader implements MappedBatchLoader<Long, Cover> {

	@NonNull
	private final CoverRepository coverRepository;

	@NonNull
	private final DelegatingSecurityContextAsyncTaskExecutor executor;

	@Override
	public CompletionStage<Map<Long, Cover>> load(Set<Long> keys) {
		return CompletableFuture.supplyAsync(
			() ->
				coverRepository
					.findAllById(keys)
					.stream()
					.collect(Collectors.toMap(Cover::getDatabaseId, Function.identity())),
			executor
		);
	}
}
