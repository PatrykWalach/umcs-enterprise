package com.umcs.enterprise.book;

import com.netflix.graphql.dgs.*;
import com.umcs.enterprise.*;
import com.umcs.enterprise.cover.CoverService;
import com.umcs.enterprise.types.BookOrderBy;
import graphql.schema.DataFetchingEnvironment;
import jakarta.persistence.EntityManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import lombok.*;
import org.mapstruct.factory.Mappers;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;

@DgsComponent
@RequiredArgsConstructor
public class BookDataFetcher {

	@NonNull
	private final BookRepository bookRepository;

	@NonNull
	private final ConnectionService connectionService;

	@DgsQuery
	public graphql.relay.Connection<Book> books(
		DataFetchingEnvironment env,
		@InputArgument List<BookOrderBy> orderBy
	) {
		List<Sort.Order> orders = Mappers
			.getMapper(BookOrderByMapper.class)
			.bookOrderByListToOrders(orderBy);

		System.out.println(orders);

		if (orders != null) {
			return connectionService.getConnection(this.bookRepository.findAll(Sort.by(orders)), env);
		}

		return connectionService.getConnection(this.bookRepository.findAll(), env);
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

		Resource resource = input.getCover().getFile().getResource();
//		System.out.println(resource.getURI());
//		System.out.println(resource.getURL());
//		System.out.println(resource.getFile());



		if (input.getCover().getFile() != null) {
			book.setCover(coverService.upload(input.getCover().getFile().getInputStream()));
		} else {
			book.setCover(coverService.upload(input.getCover().getUrl()));
		}

		return bookRepository.save(book);
	}
}
