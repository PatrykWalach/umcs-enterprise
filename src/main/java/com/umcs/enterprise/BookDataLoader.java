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

@DgsDataLoader(name = "books")
@RequiredArgsConstructor
public class BookDataLoader implements MappedBatchLoader<Long, Book> {

	private final BookRepository repository;

	@Override
	public CompletionStage<Map<Long, Book>> load(Set<Long> keys) {
		return CompletableFuture.supplyAsync(() ->
			repository
				.findAllById(keys)
				.stream()
				.collect(Collectors.toMap(Book::getDatabaseId, Function.identity()))
		);
	}
}
