package com.umcs.enterprise.book;

import com.netflix.graphql.dgs.*;
import com.umcs.enterprise.*;
import com.umcs.enterprise.cover.CoverService;
import graphql.schema.DataFetchingEnvironment;
import jakarta.persistence.EntityManager;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.BiConsumer;
import lombok.*;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;

@DgsComponent
@RequiredArgsConstructor
public class BookDataFetcher {

	@NonNull
	private final BookRepository bookRepository;

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
				.toList()
		);
	}

	@DgsQuery
	public graphql.relay.Connection<Book> books(
		DataFetchingEnvironment env,
		@InputArgument List<com.umcs.enterprise.types.BookOrderBy> orderBy
	) {
		return connectionService.getConnection(
			this.bookRepository.findAll(
					getSort(
						orderBy,
						(fields, order) -> {
							fields.put(
								"price",
								Optional
									.ofNullable(order.getPrice())
									.map(com.umcs.enterprise.types.PriceOrderBy::getRaw)
									.orElse(null)
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
		return env.getSource();
	}

	@Secured("ADMIN")
	@DgsMutation
	public Book createBook(@InputArgument com.umcs.enterprise.types.CreateBookInput input)
		throws IOException {
		Book book = Mappers.getMapper(CreateBookInputMapper.class).createBookInputToBook(input);

		if (input.getCover().getFile() != null) {
			book.setCover(coverService.upload(input.getCover().getFile()));
		} else {
			book.setCover(coverService.upload(input.getCover().getUrl()));
		}

		return bookRepository.save(book);
	}
}
