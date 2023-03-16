package com.umcs.enterprise;

import static java.lang.String.format;

import com.netflix.graphql.dgs.*;
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

	private void validateConnectionArgs(DataFetchingEnvironment env) {
		Integer first = env.getArgument("first");
		Integer last = env.getArgument("last");
		String after = env.getArgument("after");
		String before = env.getArgument("before");

		if (first == null && last == null) {
			throw new IllegalArgumentException(
				format(
					"The %s.%s connection field requires a 'first' or 'last' argument",
					env.getParentType(),
					env.getField().getName()
				)
			);
		}

		if (first != null && last != null) {
			throw new IllegalArgumentException(
				format(
					"The %s.%s connection field requires a 'first' or 'last' argument, not both",
					env.getParentType(),
					env.getField().getName()
				)
			);
		}
		if (first != null && before != null) {
			throw new IllegalArgumentException(
				format(
					"The %s.%s connection field does not allow a 'before' argument with 'first'",
					env.getParentType(),
					env.getField().getName()
				)
			);
		}
		if (last != null && after != null) {
			throw new IllegalArgumentException(
				format(
					"The %s.%s connection field does not allow a 'last' argument with 'after'",
					env.getParentType(),
					env.getField().getName()
				)
			);
		}

		if (first != null && first < 1) {
			throw new IllegalArgumentException(
				format("The page size must more than zero: 'first'=%s", first)
			);
		}

		if (last != null && last < 1) {
			throw new IllegalArgumentException(
				format("The page size must more than zero: 'last'=%s", last)
			);
		}
	}

	@DgsQuery
	public graphql.relay.Connection<Book> books(DataFetchingEnvironment env) {
		validateConnectionArgs(env);

		return new SimpleListConnection<>(this.bookRepository.findAll()).get(env);
		//        return new Connection<>(this.bookRepository::findAll, this.bookRepository::count, args);
	}

	private BookCover uploadCover(MultipartFile file) throws IOException {
		Path uploadDir = Paths.get("covers");
		if (!Files.exists(uploadDir)) {
			Files.createDirectories(uploadDir);
		}
		String filename =
			UUID.randomUUID() +
			Objects
				.requireNonNull(file.getOriginalFilename())
				.substring(file.getOriginalFilename().lastIndexOf("."));
		Path newFile = uploadDir.resolve(filename);

		try (OutputStream outputStream = Files.newOutputStream(newFile)) {
			outputStream.write(file.getBytes());
		}
		BufferedImage image = ImageIO.read(newFile.toFile());

		BookCover bookCover = new BookCover();
		bookCover.setHeight(image.getHeight());
		bookCover.setWidth(image.getWidth());

		return coverRepository.save(bookCover);
	}

	@Secured("ADMIN")
	@DgsMutation
	public Book createBook(@InputArgument CreateBookInput input, @InputArgument MultipartFile cover)
		throws IOException {
		var book = new Book();
		book.setAuthor(input.getAuthor());
		book.setCreatedAt(ZonedDateTime.now());
		book.setPrice(input.getPrice());

		if (cover != null) {
			book.setCover(uploadCover(cover));
		}

		book.setTitle(input.getTitle());
		book.setPopularity(0);
		book.setCreatedAt(ZonedDateTime.now());

		return bookRepository.save(book);
	}
}
