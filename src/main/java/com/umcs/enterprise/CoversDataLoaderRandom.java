package com.umcs.enterprise;

import com.netflix.graphql.dgs.DgsDataLoader;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;

@DgsDataLoader(name = "randomCover")
@RequiredArgsConstructor
public class CoversDataLoaderRandom implements BatchLoader<Long, Cover> {

	private final CoverRepository coverRepository;

	@Override
	public CompletionStage<List<Cover>> load(List<Long> keys) {
		return CompletableFuture.supplyAsync(() -> {
			List<Cover> covers = coverRepository.findAll();
			return keys
				.stream()
				.map(key -> new Random(key).ints(0, covers.size()).findFirst().getAsInt())
				.map(covers::get)
				.collect(Collectors.toList());
		});
	}
}
