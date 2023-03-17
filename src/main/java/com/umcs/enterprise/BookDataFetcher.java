package com.umcs.enterprise;

import static java.lang.String.format;

import com.netflix.graphql.dgs.*;
import com.umcs.enterprise.types.BookCover;
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
import java.util.Objects;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.multipart.MultipartFile;

@DgsComponent
@RequiredArgsConstructor
public class BookDataFetcher {

	private final BookRepository bookRepository;

	private final CoverRepository coverRepository;

	@DgsData(parentType = "Book")
	public DataFetcher<String> author() {
		return null;
	}

	public DataFetcher<Integer> popularity() {
		return null;
	}

	public DataFetcher<String> price() {
		return null;
	}

	private final ConnectionService connectionService;

	@DgsQuery
	public graphql.relay.Connection<Book> books(DataFetchingEnvironment env) {
		return connectionService.getConnection(this.bookRepository.findAll(), env);
	}

	private final BookCoverService bookCoverService;

	//	@Secured("ADMIN")
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
