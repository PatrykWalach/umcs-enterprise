package com.umcs.enterprise;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsDataLoader;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

@DgsDataLoader(name = "bookCovers")
@RequiredArgsConstructor
public class BookCoversDataLoader implements MappedBatchLoader<Long, BookCover> {
    private  final  CoverRepository coverRepository;

    @Override
    public CompletionStage<Map<Long, BookCover>> load(Set<Long> keys) {
return        CompletableFuture.supplyAsync(() -> coverRepository.findAllById(keys).stream().collect(Collectors.toMap(BookCover::getDatabaseId,Function.identity())));

    }
}
