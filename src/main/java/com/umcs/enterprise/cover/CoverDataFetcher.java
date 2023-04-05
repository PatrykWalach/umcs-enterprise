package com.umcs.enterprise.cover;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.InputArgument;
import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.cover.Cover;
import graphql.execution.DataFetcherResult;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;

@DgsComponent
@RequiredArgsConstructor
public class CoverDataFetcher {

	@NonNull
	private final Cloudinary cloudinary;

	@DgsData(parentType = "Cover")
	public CompletableFuture<String> url(
		DataFetchingEnvironment env,
		DgsDataFetchingEnvironment enf
	) {
		var transformation = env.<com.umcs.enterprise.types.Transformation>getSource();

		DataLoader<Long, Cover> dataLoader = enf.getDataLoader(CoverDataLoader.class);
		var book = env.<Book>getLocalContext();

		return dataLoader
			.load(book.getCover().getDatabaseId())
			.thenApply(cover ->
				cloudinary
					.url()
					.transformation(
						new Transformation<>()
							.width(transformation.getWidth())
							.height(transformation.getHeight())
							.crop(transformation.getCrop())
					)
					.generate(cover.getUuid())
			);
	}

	@DgsData(parentType = "Book")
	public DataFetcherResult<List<com.umcs.enterprise.types.Transformation>> covers(
		DataFetchingEnvironment env,
		@InputArgument List<com.umcs.enterprise.types.Transformation> transformations
	) {
		return DataFetcherResult
			.<List<com.umcs.enterprise.types.Transformation>>newResult()
			.data(transformations)
			.localContext(env.<Book>getSource())
			.build();
	}
}
