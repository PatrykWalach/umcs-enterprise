package com.umcs.enterprise.cover;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
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
					.transformation(
						new Transformation<>()
							.gravity(getGravity(transformation.getGravity()))
							.aspectRatio(
								Optional
									.ofNullable(transformation.getAspectRatio())
									.map(aspect -> "" + aspect.getWidth() + ":" + aspect.getHeight())
									.orElse(null)
							)
							.width(transformation.getWidth())
							.height(transformation.getHeight())
							.crop(getCrop(transformation.getCrop()))
							.quality(new QualityAdapter(transformation.getQuality()).transform())
							.fetchFormat(getFormat(transformation.getFormat()))
					)
					.generate(cover.getUuid())
			);
	}

	@RequiredArgsConstructor
	static class QualityAdapter {

		private final Quality quality;

		public Object transform() {
			if (quality == null) {
				return null;
			}

			if (quality.getPercentage() != null) {
				return quality.getPercentage();
			}

			if (quality.getAuto() == null) {
				return null;
			}

			return switch (quality.getAuto()) {
				case DEFAULT -> "auto";
				case BEST -> "auto:best";
				case ECO -> "auto:eco";
				case GOOD -> "auto:good";
				case LOW -> "auto:low";
			};
		}
	}

	public String getGravity(Gravity gravity) {
		if (gravity == null) {
			return null;
		}
		return switch (gravity) {
			case AUTO -> "auto";
		};
	}

	public String getCrop(Crop crop) {
		if (crop == null) {
			return null;
		}

		return switch (crop) {
			case FILL -> " fill";
			case FIT -> " fit";
			case FILL_PAD -> " fill_pad";
			case IMAGGA_CROP -> " imagga_crop";
			case IMAGGA_SCALE -> " imagga_scale";
			case CROP -> "  crop";
			case LFILL -> " lfill";
			case LIMIT -> "  limit";
			case LPAD -> " lpad";
			case MFIT -> " mfit";
			case MPAD -> " mpad";
			case PAD -> " pad";
			case SCALE -> " scale";
			case THUMB -> " thumb";
		};
	}

	public String getFormat(Format format) {
		if (format == null) {
			return null;
		}

		return switch (format) {
			case BMP -> "bmp";
			case GIF -> "gif";
			case JPEG_2000 -> "jp2";
			case JPEG_XR -> "jxr";
			case AUTO -> "auto";
			case JPG -> "jpg";
			case PNG -> "png";
			case WEBP -> "webp";
		};
	}

	@DgsData(parentType = "Book")
	public DataFetcherResult<List<com.umcs.enterprise.types.Transformation>> covers(
		DataFetchingEnvironment env,
		@InputArgument com.umcs.enterprise.types.Transformation transformation,
		@InputArgument List<Integer> widths
	) {
		if (transformation == null) {
			transformation = new com.umcs.enterprise.types.Transformation();
		}

		com.umcs.enterprise.types.Transformation finalTransformation = transformation;

		return DataFetcherResult
			.<List<com.umcs.enterprise.types.Transformation>>newResult()
			.data(
				widths
					.stream()
					.map(width ->
						com.umcs.enterprise.types.Transformation
							.newBuilder()
							.crop(finalTransformation.getCrop())
							.gravity(finalTransformation.getGravity())
							.format(finalTransformation.getFormat())
							.height(finalTransformation.getHeight())
							.width(width)
							.quality(finalTransformation.getQuality())
							.aspectRatio(finalTransformation.getAspectRatio())
							.build()
					)
					.collect(Collectors.toList())
			)
			.localContext(env.<Book>getSource())
			.build();
	}
}
