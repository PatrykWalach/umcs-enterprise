package com.umcs.enterprise.cover;

import com.cloudinary.Cloudinary;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.InputArgument;
import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.types.*;
import graphql.execution.DataFetcherResult;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.mapstruct.factory.Mappers;

@DgsComponent
@RequiredArgsConstructor
public class CoverDataFetcher {

	@NonNull
	private final Cloudinary cloudinary;

	@DgsData(parentType = "Cover")
	public CompletableFuture<String> url(DgsDataFetchingEnvironment env) {
		var transformation = env.<com.umcs.enterprise.types.Transformation>getSource();

		DataLoader<Long, Cover> dataLoader = env.getDataLoader(CoverDataLoader.class);
		var book = env.<Book>getLocalContext();

		return dataLoader
			.load(book.getCover().getDatabaseId())
			.thenApply(cover ->
				cloudinary
					.url()
					.transformation(Mappers.getMapper(TransformationMapper.class).map(transformation))
					.generate(cover.getUuid())
			);
	}

	@DgsData(parentType = "Book")
	public DataFetcherResult<List<com.umcs.enterprise.types.Transformation>> covers(
		DataFetchingEnvironment env,
		@InputArgument com.umcs.enterprise.types.Transformation transformation,
		@InputArgument List<Integer> widths
	) {
		return DataFetcherResult
			.<List<com.umcs.enterprise.types.Transformation>>newResult()
			.data(
				widths
					.stream()
					.map(width -> {
						Transformation copy = Mappers.getMapper(TransformationMapper.class).copy(
								Optional.ofNullable(transformation).orElse(new Transformation())
						);
						copy.setWidth(width);
						return copy;
					})
					.collect(Collectors.toList())
			)
			.localContext(env.<Book>getSource())
			.build();
	}
}
