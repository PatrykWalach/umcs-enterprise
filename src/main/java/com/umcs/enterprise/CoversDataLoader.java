package com.umcs.enterprise;

import com.netflix.graphql.dgs.DgsDataLoader;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;

@DgsDataLoader(name = "bookCovers")
@RequiredArgsConstructor
public class CoversDataLoader implements MappedBatchLoader<Long, Cover> {

	private final CoverRepository coverRepository;

	@Override
	public CompletionStage<Map<Long, Cover>> load(Set<Long> keys) {
		return CompletableFuture.supplyAsync(() ->
			coverRepository
				.findAllById(keys)
				.stream()
				.collect(Collectors.toMap(Cover::getDatabaseId, Function.identity()))
		);
	}
}
