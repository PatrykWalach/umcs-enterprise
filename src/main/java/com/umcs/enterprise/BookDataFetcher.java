package com.umcs.enterprise;

import static java.lang.String.format;

import com.netflix.graphql.dgs.*;
import com.umcs.enterprise.types.BookSortBy;
import com.umcs.enterprise.types.CreateBookInput;
import graphql.relay.SimpleListConnection;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
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

	//	@DgsData(parentType = "Book")
	//	public BookCover cover(DataFetchingEnvironment env) {
	//		return  env.<Book>getSource().getCover();
	//	}

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
				),
			env
		);
	}

	private final BookCoverService bookCoverService;

	@Secured("ADMIN")
	@DgsMutation
	public Book createBook(@InputArgument CreateBookInput input) throws IOException {
		var book = new Book();
		book.setAuthor(input.getAuthor());
		book.setCreatedAt(ZonedDateTime.now());
		book.setPrice(input.getPrice());

		MultipartFile cover = input.getCover();

		if (cover != null) {
			book.setCover(bookCoverService.uploadCover(cover));
		}

		book.setTitle(input.getTitle());
		book.setPopularity(0);
		book.setCreatedAt(ZonedDateTime.now());

		return bookRepository.save(book);
	}
}
