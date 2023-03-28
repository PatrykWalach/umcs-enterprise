package com.umcs.enterprise.book;

import com.netflix.graphql.dgs.*;
import com.umcs.enterprise.*;
import com.umcs.enterprise.cover.CoverDataLoader;
import com.umcs.enterprise.cover.CoverService;
import com.umcs.enterprise.types.*;
import graphql.schema.DataFetchingEnvironment;
import jakarta.persistence.EntityManager;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import lombok.*;
import org.dataloader.DataLoader;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.multipart.MultipartFile;

@DgsComponent
@RequiredArgsConstructor
public class BookDataFetcher {

	@NonNull
	private final BookRepository bookRepository;

	@DgsData(parentType = "Book")
	public CompletableFuture<Cover> cover(
		DgsDataFetchingEnvironment enf,
		DataFetchingEnvironment env
	) {
		DataLoader<Long, Cover> dataLoader = enf.getDataLoader(CoverDataLoader.class);

		return Optional
			.ofNullable(env.<Book>getSource().getCover())
			.map(com.umcs.enterprise.cover.Cover::getDatabaseId)
			.map(dataLoader::load)
			.orElse(null);
	}

	@NonNull
	private final ConnectionService connectionService;

	private <T> Sort getSort(
		List<T> orders,
		BiConsumer<HashMap<String, com.umcs.enterprise.types.Order>, T> fn
	) {
		return Sort.by(
			Optional
				.ofNullable(orders)
				.orElse(new ArrayList<>())
				.stream()
				.map(orderBy -> {
					HashMap<String, com.umcs.enterprise.types.Order> fields = new HashMap<>();
					fn.accept(fields, orderBy);
					return fields;
				})
				.flatMap(fields ->
					fields
						.entrySet()
						.stream()
						.filter(e -> e.getValue() != null)
						.map(e -> {
							if (e.getValue() == com.umcs.enterprise.types.Order.ASC) {
								return Sort.Order.asc(e.getKey());
							}
							return Sort.Order.desc(e.getKey());
						})
				)
				.collect(Collectors.toList())
		);
	}

	@DgsQuery
	public graphql.relay.Connection<Book> books(
		DataFetchingEnvironment env,
		@InputArgument List<BookOrderBy> orderBy
	) {
		return connectionService.getConnection(
			this.bookRepository.findAll(
					getSort(
						orderBy,
						(fields, order) -> {
							fields.put(
								"price",
								Optional.ofNullable(order.getPrice()).map(PriceOrderBy::getRaw).orElse(null)
							);
							fields.put("popularity", order.getPopularity());
							fields.put("releasedAt", order.getReleasedAt());
						}
					)
				),
			env
		);
	}

	@NonNull
	private final EntityManager manager;

	@DgsData(parentType = "Book")
	public graphql.relay.Connection<Book> recommended(DataFetchingEnvironment env) {
		return connectionService.getConnection(
			this.bookRepository.findByOrdersBooksDatabaseId(env.<Book>getSource().getDatabaseId()),
			env
		);
	}

	@NonNull
	private final CoverService coverService;

	@DgsData(parentType = "CreateBookResult")
	public Book book(DataFetchingEnvironment env) {
		return env.<Book>getSource();
	}

	@Secured("ADMIN")
	@DgsMutation
	public Book createBook(@InputArgument CreateBookInput input) throws IOException {
		var book = new Book();
		book.setAuthor(input.getAuthor());
		book.setCreatedAt(ZonedDateTime.now());
		book.setPrice(BigDecimal.valueOf(input.getPrice().getRaw()));

		MultipartFile cover = input.getCover();

		if (cover != null) {
			book.setCover(coverService.uploadCover(cover));
		}

		book.setPopularity(0L);
		book.setTitle(input.getTitle());
		book.setCreatedAt(ZonedDateTime.now());

		return bookRepository.save(book);
	}
}
