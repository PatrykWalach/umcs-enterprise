package com.umcs.enterprise;

import static com.netflix.graphql.types.errors.ErrorType.PERMISSION_DENIED;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.umcs.enterprise.types.BookSortBy;
import com.umcs.enterprise.types.CreateBookInput;
import io.jsonwebtoken.Jwts;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.assertj.core.util.Streams;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.graphql.client.GraphQlTransport;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.WebGraphQlTester;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = EnterpriseApplication.class)
class BookDataFetcherTest {

	@Autowired
	private WebGraphQlTester graphQlTester;

	@Autowired
	private BookRepository bookRepository;

	@BeforeEach
	void beforeEach() {
		userRepository.deleteAll();
		bookOrderRepository.deleteAll();
		orderRepository.deleteAll();
		bookRepository.deleteAll();
	}

	@Test
	void createBook_admin() {
		//        given
		var user = userRepository.save(
			User
				.newBuilder()
				.authorities(Collections.singletonList(("ADMIN")))
				.password(passwordEncoder.encode("user"))
				.username("user")
				.build()
		);
		String token = jwtService.signToken(
			Jwts.builder().setClaims(new HashMap<>()).setSubject(user.getUsername())
		);

		var input = new CreateBookInput.Builder()
			.title("The Book")
			.author("The Author")
			.price(100.0)
			.build();

		this.graphQlTester.mutate()
			.header("Authorization", "Bearer " + token)
			.build()
			.documentName("BookControllerTest_createBook")
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
			.isEqualTo("100,00 zł")
			.path("createBook.popularity")
			.entity(Integer.class)
			.isEqualTo(0);

		Assert.isTrue(bookRepository.count() == 1, "There should be one book");
	}

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;

	@Test
	void createBook_user() {
		//        given
		var user = userRepository.save(
			User
				.newBuilder()
				.authorities(Collections.singletonList(("USER")))
				.password(passwordEncoder.encode("user"))
				.username("user")
				.build()
		);
		String token = jwtService.signToken(
			Jwts.builder().setClaims(new HashMap<>()).setSubject(user.getUsername())
		);

		var input = new CreateBookInput.Builder()
			.title("The Book")
			.author("The Author")
			.price(100.0)
			.build();

		this.graphQlTester.mutate()
			.header("Authorization", "Bearer " + token)
			.build()
			.documentName("BookControllerTest_createBook")
			.variable("input", input)
			//        when
			.execute()
			//                then
			.errors()
			.expect(e -> true)
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
	) {
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
	private OrderRepository orderRepository;

	@Test
	void recommended() {
		//        given

		Book book = bookRepository.save(Book.newBuilder().build());
		bookRepository.saveAll(
			IntStream
				.range(0, 10)
				.mapToObj(i -> Book.newBuilder().title("" + i).build())
				.collect(Collectors.toList())
		);
		var recommended = bookRepository.saveAll(
			IntStream
				.range(0, 7)
				.mapToObj(i -> Book.newBuilder().title("" + (10 + i)).build())
				.collect(Collectors.toList())
		);

		List<Order> orders = orderRepository.saveAll(Stream.generate(Order::new).limit(4).toList());

		ArrayList<List<Book>> slices = new ArrayList<>();
		slices.add(recommended.subList(0, 5));
		slices.add(recommended.subList(2, 4));
		slices.add(recommended.subList(3, 7));
		slices.add(recommended.subList(1, 6));

		bookOrderRepository.saveAll(
			IntStream
				.range(0, slices.size())
				.boxed()
				.flatMap(i ->
					Stream
						.concat(slices.get(i).stream(), Stream.of(book))
						.map(book1 -> BookOrder.newBuilder().book(book1).order(orders.get(i)).build())
				)
				.toList()
		);

		this.graphQlTester.documentName("BookControllerTest_recommended")
			.variable("id", book.getId())
			//        when
			.execute()
			//                then
			.errors()
			.verify()
			.path("node.recommended.edges[*].node.title")
			.entity(List.class)
			.isEqualTo(List.of("13", "12", "14", "11", "15", "10", "16"));
	}

	@Autowired
	private BookOrderRepository bookOrderRepository;

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
	void books_sort(String order, String out0, String out1, String out2, String out3) { //        given
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

		book3.setPrice(BigDecimal.valueOf(50));
		book0.setPrice(BigDecimal.valueOf(60));
		book1.setPrice(BigDecimal.valueOf(75));
		book2.setPrice(BigDecimal.valueOf(120));

		bookRepository.saveAll(Arrays.asList(book0, book1, book2, book3));

		//		book2.popularity=null
		bookOrderRepository.saveAll(
			orderRepository
				.saveAll(Stream.generate(Order::new).limit(25).collect(Collectors.toList()))
				.stream()
				.map(o -> BookOrder.newBuilder().book(book1).order(o).build())
				.toList()
		);
		bookOrderRepository.saveAll(
			orderRepository
				.saveAll(Stream.generate(Order::new).limit(30).collect(Collectors.toList()))
				.stream()
				.map(o -> BookOrder.newBuilder().book(book3).order(o).build())
				.toList()
		);
		bookOrderRepository.saveAll(
			orderRepository
				.saveAll(Stream.generate(Order::new).limit(35).collect(Collectors.toList()))
				.stream()
				.map(o -> BookOrder.newBuilder().book(book0).order(o).build())
				.toList()
		);

		this.graphQlTester.documentName("BookControllerTest_books")
			.variable("first", 4)
			.variable("sortBy", sort.get(order))
			//        when
			.execute()
			//                then
			.errors()
			.verify()
			.path("books.edges[*].node.title")
			.entity(List.class)
			.isEqualTo(List.of(out0, out1, out2, out3));
	}

	@ParameterizedTest
	@CsvSource({ "-1,,,", ",,-1,", "10,,10,", ",10,,10", "10,,10,", ",10,10,", "0,,,", ",,0," })
	void books_variables(Integer first, String after, Integer last, String before) {
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
	void books_empty() {
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
	void cover() {
		//        given
		Cover cover = coverRepository.save(
			Cover.newBuilder().width(12).height(15).filename("file.jpg").build()
		);
		Book book = bookRepository.save(Book.newBuilder().cover(cover).build());

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
