package com.umcs.enterprise;

import static com.netflix.graphql.types.errors.ErrorType.PERMISSION_DENIED;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.umcs.enterprise.types.BookSortBy;
import com.umcs.enterprise.types.CreateBookInput;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.util.Assert;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = EnterpriseApplication.class)
class BookDataFetcherTest {

	@Autowired
	private GraphQlTester graphQlTester;

	@Autowired
	private BookRepository bookRepository;

	@BeforeEach
	void beforeEach() {
		orderRepository.deleteAll();
		bookRepository.deleteAll();
	}

	@Test
	@Disabled("TODO: implement JWT")
	void createBook_admin() throws JSONException {
		//        given
		var input = new CreateBookInput.Builder()
			.title("The Book")
			.author("The Author")
			.price(100)
			.build();

		this.graphQlTester.documentName("BookControllerTest_createBook")
			.variable("input", input)
			//        when
			.execute()
			//                then
			.errors()
			.verify()
			.path("createBook.title")
			.entity(String.class)
			.isEqualTo(input.getTitle())
			.path("createBook.author")
			.entity(String.class)
			.isEqualTo(input.getAuthor())
			.path("createBook.price")
			.entity(String.class)
			.isEqualTo(input.getPrice().toString())
			.path("createBook.popularity")
			.entity(Integer.class)
			.isEqualTo(0);

		Assert.isTrue(bookRepository.count() == 1, "There should be one book");
	}

	@Test
	void createBook_user() throws JSONException {
		//        given
		var input = new CreateBookInput.Builder()
			.title("The Book")
			.author("The Author")
			.price(100)
			.build();

		this.graphQlTester.documentName("BookControllerTest_createBook")
			.variable("input", input)
			//        when
			.execute()
			//                then
			.errors()
			.expect(e -> e.getErrorType() == PERMISSION_DENIED)
			.verify()
			.path("createBook")
			.valueIsNull();

		Assert.isTrue(bookRepository.count() == 0, "There should be no books");
	}

	@ParameterizedTest
	@CsvSource(
		{
			"10,5,,,false,true,6,13",
			"10,,,,true,false,0,9",
			"10,9,,,false,true,10,13",
			",,10,,false,true,4,13",
			",,10,10,true,false,0,9"
		}
	)
	void books(
		Integer first,
		String after,
		Integer last,
		String before,
		boolean hasNextPage,
		boolean hasPreviousPage,
		String startCursor,
		String endCursor
	) throws JSONException {
		//        given
		bookRepository.saveAll(
			IntStream.range(0, 14).mapToObj(i -> new Book()).collect(Collectors.toList())
		);

		this.graphQlTester.documentName("BookControllerTest_books")
			.variable("first", first)
			.variable("after", encode(after))
			.variable("last", last)
			.variable("before", encode(before))
			//        when
			.execute()
			//                then
			.errors()
			.verify()
			.path("books.pageInfo.startCursor")
			.entity(String.class)
			.isEqualTo(encode(startCursor))
			.path("books.pageInfo.endCursor")
			.entity(String.class)
			.isEqualTo(encode(endCursor))
			.path("books.pageInfo.hasNextPage")
			.entity(Boolean.class)
			.isEqualTo(hasNextPage)
			.path("books.pageInfo.hasPreviousPage")
			.entity(Boolean.class)
			.isEqualTo(hasPreviousPage);
	}

	private String encode(String c) {
		return c == null
			? null
			: Base64.getEncoder().encodeToString(("simple-cursor" + c).getBytes(StandardCharsets.UTF_8));
	}

	@Autowired
	private  OrderRepository orderRepository;

@Test
	void recommended(){
		//        given

		Book book   = bookRepository.save(Book.newBuilder().build());
		bookRepository.saveAll(IntStream.range(0,10).mapToObj((i)-> Book.newBuilder().title(""+i).build()).collect(Collectors.toList()));
		var recommended   = bookRepository.saveAll(IntStream.range(0,7).mapToObj((i)-> Book.newBuilder().title(""+(10+i)).build()).collect(Collectors.toList()));

		orderRepository.saveAll(List.of(
				Order.newBuilder().books(Stream.concat(recommended.subList(0, 5).stream(), Stream.of(book)).collect(Collectors.toSet())).build(),
				Order.newBuilder().books(Stream.concat(recommended.subList(2, 4).stream(), Stream.of(book)).collect(Collectors.toSet())).build(),
				Order.newBuilder().books(Stream.concat(recommended.subList(3, 7).stream(), Stream.of(book)).collect(Collectors.toSet())).build(),
				Order.newBuilder().books(Stream.concat(recommended.subList(1, 6).stream(), Stream.of(book)).collect(Collectors.toSet())).build()
		));





	this.graphQlTester.documentName("BookControllerTest_recommended")
				.variable("id", book.getId())
				//        when
				.execute()
				//                then
				.errors()
				.verify()
				.path("node.recommended.edges[*].node.title")
				.entity(List.class)
				.isEqualTo(List.of("13","12","14", "11","15" ,"10","16"));
	}

	@ParameterizedTest
	@CsvSource(
		{
			",Book:0,Book:1,Book:2,Book:3",
			"price_ASC,Book:3,Book:0,Book:1,Book:2",
			"price_DESC,Book:2,Book:1,Book:0,Book:3",
			"popularity_ASC,Book:2,Book:1,Book:3,Book:0",
			"popularity_DESC,Book:0,Book:3,Book:1,Book:2",
			"releasedAt_ASC,Book:2,Book:3,Book:1,Book:0",
			"releasedAt_DESC,Book:0,Book:1,Book:3,Book:2"
		}
	)
	void books_sort(String order, String out0, String out1, String out2, String out3)
		throws JSONException { //        given
		HashMap<String, BookSortBy> sort = new HashMap<>();
		sort.put(
			"price_ASC",
			BookSortBy.newBuilder().price(com.umcs.enterprise.types.Sort.ASC).build()
		);
		sort.put(
			"price_DESC",
			BookSortBy.newBuilder().price(com.umcs.enterprise.types.Sort.DESC).build()
		);
		sort.put(
			"popularity_ASC",
			BookSortBy.newBuilder().popularity(com.umcs.enterprise.types.Sort.ASC).build()
		);
		sort.put(
			"popularity_DESC",
			BookSortBy.newBuilder().popularity(com.umcs.enterprise.types.Sort.DESC).build()
		);
		sort.put(
			"releasedAt_ASC",
			BookSortBy.newBuilder().releasedAt(com.umcs.enterprise.types.Sort.ASC).build()
		);
		sort.put(
			"releasedAt_DESC",
			BookSortBy.newBuilder().releasedAt(com.umcs.enterprise.types.Sort.DESC).build()
		);

		Book book0 = new Book();
		Book book1 = new Book();
		Book book2 = new Book();
		Book book3 = new Book();

		book0.setTitle("Book:0");
		book1.setTitle("Book:1");
		book2.setTitle("Book:2");
		book3.setTitle("Book:3");

		book2.setReleasedAt(ZonedDateTime.now());
		book3.setReleasedAt(ZonedDateTime.now().plusDays(1));
		book1.setReleasedAt(ZonedDateTime.now().plusDays(2));
		book0.setReleasedAt(ZonedDateTime.now().plusDays(3));

		book2.setPopularity(20L);
		book1.setPopularity(25L);
		book3.setPopularity(30L);
		book0.setPopularity(35L);

		book3.setPrice(50);
		book0.setPrice(60);
		book1.setPrice(75);
		book2.setPrice(120);

		bookRepository.saveAll(Arrays.asList(book0, book1, book2, book3));

		this.graphQlTester.documentName("BookControllerTest_books")
			.variable("first", 4)
			.variable("sortBy", sort.get(order))
			//        when
			.execute()
			//                then
			.errors()
			.verify()
			.path("books.edges[0].node.title")
			.entity(String.class)
			.isEqualTo(out0)
			.path("books.edges[1].node.title")
			.entity(String.class)
			.isEqualTo(out1)
			.path("books.edges[2].node.title")
			.entity(String.class)
			.isEqualTo(out2)
			.path("books.edges[3].node.title")
			.entity(String.class)
			.isEqualTo(out3);
	}

	@ParameterizedTest
	@CsvSource({ "-1,,,", ",,-1,", "10,,10,", ",10,,10", "10,,10,", ",10,10,", "0,,,", ",,0," })
	void books_variables(Integer first, String after, Integer last, String before)
		throws JSONException {
		//        given

		this.graphQlTester.documentName("BookControllerTest_books")
			.variable("first", first)
			.variable("before", before)
			.variable("after", after)
			.variable("last", last)
			//        when
			.execute()
			//                then
			.errors()
			.expect(e -> true)
			.verify()
			.path("books")
			.valueIsNull();
	}

	@Test
	void books_empty() throws JSONException {
		//        given

		this.graphQlTester.documentName("BookControllerTest_books")
			.variable("last", 10)
			//        when
			.execute()
			//                then
			.errors()
			.verify()
			.path("books.pageInfo.hasNextPage")
			.entity(Boolean.class)
			.isEqualTo(false)
			.path("books.pageInfo.hasPreviousPage")
			.entity(Boolean.class)
			.isEqualTo(false)
			.path("books.pageInfo.startCursor")
			.valueIsNull()
			.path("books.pageInfo.endCursor")
			.valueIsNull();
	}

	@Autowired
	private CoverRepository coverRepository;

	@Test
	void cover() throws JSONException {
		//        given
		Cover cover = coverRepository.save(Cover.newBuilder().width( 12).height(15).filename( "file.jpg").build() );
		Book book   = bookRepository.save(Book.newBuilder().cover(cover).build());


		this.graphQlTester.documentName("BookControllerTest_bookCover")
			.variable("id", book.getId())
			//        when
			.execute()
			//                then
			.errors()
			.verify()
			.path("node.cover.width")
			.entity(Integer.class)
			.isEqualTo(cover.getWidth())
			.path("node.cover.height")
			.entity(Integer.class)
			.isEqualTo(cover.getHeight());
	}
}
