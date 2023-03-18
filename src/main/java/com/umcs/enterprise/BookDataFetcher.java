package com.umcs.enterprise;

import static java.lang.String.format;

import com.netflix.graphql.dgs.*;
import com.umcs.enterprise.types.BookSortBy;
import com.umcs.enterprise.types.Cover;
import com.umcs.enterprise.types.CreateBookInput;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.multipart.MultipartFile;

@DgsComponent
@RequiredArgsConstructor
public class BookDataFetcher {

	private final BookRepository bookRepository;

	private final CoverRepository coverRepository;

	//	@DgsData(parentType = "Book")
	//	public DataFetcher<String> author() {
	//		return null;
	//	}

	@DgsData(parentType = "Book")
	public DataFetcher<Integer> popularity() {
		return null;
	}

	public DataFetcher<String> price() {
		Currency pln = Currency.getInstance("PLN");

		return null;
	}

	@DgsData(parentType = "Book")
	public CompletableFuture<Cover> cover(
		DgsDataFetchingEnvironment enf,
		DataFetchingEnvironment env
	) {
		DataLoader<Long, Cover> dataLoader = enf.getDataLoader(CoversDataLoader.class);
		return Optional.ofNullable(env.<Book>getSource().getCover()).map(com.umcs.enterprise.Cover::getDatabaseId).map( dataLoader::load).orElse(null);
	}

	private final ConnectionService connectionService;

	private <T> Sort getSort(
		List<T> sortBys,
		BiConsumer<HashMap<String, com.umcs.enterprise.types.Sort>, T> fn
	) {
		return Sort.by(
			Optional
				.ofNullable(sortBys)
				.orElse(new ArrayList<>())
				.stream()
				.map(sortBy -> {
					HashMap<String, com.umcs.enterprise.types.Sort> fields = new HashMap<>();
					fn.accept(fields, sortBy);
					return fields;
				})
				.flatMap(fields ->
					fields
						.entrySet()
						.stream()
						.filter(e -> e.getValue() != null)
						.map(e -> {
							if (e.getValue() == com.umcs.enterprise.types.Sort.ASC) {
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
		@InputArgument List<BookSortBy> sortBy
	) {

		return connectionService.getConnection(


			this.bookRepository.findAll(
					getSort(
						sortBy,
						(fields, order) -> {
							fields.put("price", order.getPrice());
							fields.put("popularity", order.getPopularity());
							fields.put("releasedAt", order.getReleasedAt());
						}
					)
				)
				,
			env
		);
	}

	private final EntityManager manager;

	@DgsData(parentType = "Book")
	public graphql.relay.Connection<Book> recommended(
			DataFetchingEnvironment env
	) {

		return connectionService.getConnection(

			 	this.bookRepository.findByOrdersBooksDatabaseId(
					env.<Book>getSource().getDatabaseId())
,
				env
		);
	}

	private final CoverService coverService;

	@Secured("ADMIN")
	@DgsMutation
	public Book createBook(@InputArgument CreateBookInput input) throws IOException {
		var book = new Book();
		book.setAuthor(input.getAuthor());
		book.setCreatedAt(ZonedDateTime.now());
		book.setPrice(input.getPrice());

		MultipartFile cover = input.getCover();

		if (cover != null) {
			book.setCover(coverService.uploadCover(cover));
		}

		book.setTitle(input.getTitle());
		book.setPopularity(0L);
		book.setCreatedAt(ZonedDateTime.now());

		return bookRepository.save(book);
	}
}
