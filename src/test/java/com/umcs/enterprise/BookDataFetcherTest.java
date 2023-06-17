package com.umcs.enterprise;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cloudinary.Cloudinary;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.cover.Cover;
import com.umcs.enterprise.purchase.*;
import com.umcs.enterprise.purchase.Purchase;
import com.umcs.enterprise.types.*;
import com.umcs.enterprise.user.User;
import com.umcs.enterprise.user.UserService;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(classes = EnterpriseApplication.class)
@ExtendWith(CleanDb.class)
@AutoConfigureMockMvc
class BookDataFetcherTest {

	@Autowired
	WebApplicationContext context;

	@BeforeEach
	public void beforeEach() {
		WebTestClient client = MockMvcWebTestClient
			.bindToApplicationContext(context)
			.configureClient()
			.baseUrl("/graphql")
			.build();

		graphQlTester = HttpGraphQlTester.create(client);
	}

	private HttpGraphQlTester graphQlTester;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private MockMvc mvc;

	@Test
	@WithMockUser(username = "user", authorities = { "ADMIN" })
	void createBook_admin() throws Exception {
		//        given
		var user = userRepository.save(
			User
				.newBuilder()
				.authorities(Collections.singletonList(("ADMIN")))
				.password("user")
				.username("user")
				.build()
		);

		var input = new LinkedHashMap<>();
		var coverInput = new LinkedHashMap<>();
		coverInput.put("file", null);
		input.put("title", ("The Book"));
		input.put("author", ("The Author"));
		input.put("releasedAt", (OffsetDateTime.now().toString()));
		input.put("cover", (coverInput));
		input.put("price", (CreatePriceInput.newBuilder().raw(100.0).build()));

		Resource file = new ClassPathResource("cover.jpg");

		String query = new ClassPathResource("graphql-test/BookControllerTest_createBook.graphql")
			.getContentAsString(StandardCharsets.UTF_8);

		//        when
		this.mvc.perform(
				multipart("/graphql")
					.file(
						new MockMultipartFile(
							"0",
							file.getFilename(),
							MediaType.IMAGE_JPEG_VALUE,
							file.getInputStream()
						)
					)
					.param(
						"operations",
						new ObjectMapper()
							.writeValueAsString(Map.of("query", query, "variables", Map.of("input", input)))
					)
					.param(
						"map",
						new ObjectMapper()
							.writeValueAsString(Map.of("0", List.of("variables.input.cover.file")))
					)
					.header("graphql-require-preflight", "")
			)
			//                then
			.andExpect(status().isOk())
			.andExpect(jsonPath("errors").doesNotExist())
			.andExpect(jsonPath("data.createBook.book.title").value(input.get("title")))
			.andExpect(jsonPath("data.createBook.book.author").value(input.get("author")))
			.andExpect(
				jsonPath("data.createBook.book.price.raw")
					.value(((CreatePriceInput) input.get("price")).getRaw())
			)
			.andExpect(jsonPath("data.createBook.book.price.formatted").isNotEmpty())
			.andExpect(jsonPath("data.createBook.book.popularity").value(0))
			.andExpect(jsonPath("data.createBook.book.covers[0].url").isNotEmpty());

		Assertions.assertEquals(1L, bookRepository.count());
	}

	@Autowired
	private UserService userRepository;

	@Test
	@WithMockUser(username = "user")
	void createBook_user() throws Exception {
		//        given
		var user = userRepository.save(
			User
				.newBuilder()
				.authorities(Collections.singletonList(("USER")))
				.password("user")
				.username("user")
				.build()
		);

		var input = new LinkedHashMap<>();
		var coverInput = new LinkedHashMap<>();
		coverInput.put("file", null);
		input.put("title", ("The Book"));
		input.put("author", ("The Author"));
		input.put("releasedAt", (OffsetDateTime.now().toString()));
		input.put("cover", (coverInput));
		input.put("price", (CreatePriceInput.newBuilder().raw(100.0).build()));

		Resource file = new ClassPathResource("cover.jpg");

		String query = new ClassPathResource("graphql-test/BookControllerTest_createBook.graphql")
			.getContentAsString(StandardCharsets.UTF_8);

		//        when
		this.mvc.perform(
				multipart("/graphql")
					.file(
						new MockMultipartFile(
							"0",
							file.getFilename(),
							MediaType.IMAGE_JPEG_VALUE,
							file.getInputStream()
						)
					)
					.param(
						"operations",
						new ObjectMapper()
							.writeValueAsString(Map.of("query", query, "variables", Map.of("input", input)))
					)
					.param(
						"map",
						new ObjectMapper()
							.writeValueAsString(Map.of("0", List.of("variables.input.cover.file")))
					)
					.header("graphql-require-preflight", "")
			)
			//                then
			.andExpect(status().isOk())
			.andExpect(jsonPath("errors").isArray())
			.andExpect(jsonPath("data.createBook").isEmpty());

		Assertions.assertEquals(0L, bookRepository.count());
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

		bookRepository.saveAll(IntStream.range(0, 14).mapToObj(i -> new Book()).toList());

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
	private PurchaseService purchaseRepository;

	@Test
	void recommended() throws JsonProcessingException {
		//        given

		Book book = bookRepository.save(new Book());
		bookRepository.saveAll(
			IntStream.range(0, 10).mapToObj(i -> Book.newBuilder().title("" + i).build()).toList()
		);
		var recommended = bookRepository.saveAll(
			IntStream.range(0, 7).mapToObj(i -> Book.newBuilder().title("" + (10 + i)).build()).toList()
		);

		List<Purchase> purchases = purchaseRepository.saveAll(
			Stream.generate(Purchase::new).limit(4).toList()
		);

		ArrayList<List<Book>> slices = new ArrayList<>();
		slices.add(recommended.subList(0, 5));
		slices.add(recommended.subList(2, 4));
		slices.add(recommended.subList(3, 7));
		slices.add(recommended.subList(1, 6));

		bookPurchaseRepository.saveAll(
			IntStream
				.range(0, slices.size())
				.boxed()
				.flatMap(i ->
					Stream
						.concat(slices.get(i).stream(), Stream.of(book))
						.map(book1 ->
							BookPurchase
								.newBuilder()
								.databaseId(
									new BookPurchaseId(purchases.get(i).getDatabaseId(), book1.getDatabaseId())
								)
								.book(book1)
								.purchase(purchases.get(i))
								.build()
						)
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

	@Test
	void recommended_2() throws JsonProcessingException {
		//        given

		var recommended = bookRepository.saveAll(
			IntStream.range(0, 2).mapToObj(i -> Book.newBuilder().title("" + (i)).build()).toList()
		);

		Book book = bookRepository.save(new Book());

		Purchase purchase = purchaseRepository.save(new Purchase());

		bookPurchaseRepository.saveAll(
			Stream
				.concat(recommended.stream(), Stream.of(book))
				.map(book1 -> BookPurchase.newBuilder().book(book1).purchase(purchase).build())
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
			.isEqualTo(List.of("0", "1"));
	}

	@Autowired
	private BookPurchaseRepository bookPurchaseRepository;

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
	void books_sort(String order, String out0, String out1, String out2, String out3) {
		//        given

		HashMap<String, BookOrderBy> sort = new HashMap<>();
		sort.put(
			"price_ASC",
			BookOrderBy.newBuilder().price(new PriceOrderBy(com.umcs.enterprise.types.Order.ASC)).build()
		);
		sort.put(
			"price_DESC",
			BookOrderBy.newBuilder().price(new PriceOrderBy(com.umcs.enterprise.types.Order.DESC)).build()
		);
		sort.put(
			"popularity_ASC",
			BookOrderBy.newBuilder().popularity(com.umcs.enterprise.types.Order.ASC).build()
		);
		sort.put(
			"popularity_DESC",
			BookOrderBy.newBuilder().popularity(com.umcs.enterprise.types.Order.DESC).build()
		);
		sort.put(
			"releasedAt_ASC",
			BookOrderBy.newBuilder().releasedAt(com.umcs.enterprise.types.Order.ASC).build()
		);
		sort.put(
			"releasedAt_DESC",
			BookOrderBy.newBuilder().releasedAt(com.umcs.enterprise.types.Order.DESC).build()
		);

		Book book0 = new Book();
		Book book1 = new Book();
		Book book2 = new Book();
		Book book3 = new Book();

		book0.setTitle("Book:0");
		book1.setTitle("Book:1");
		book2.setTitle("Book:2");
		book3.setTitle("Book:3");

		book2.setReleasedAt(OffsetDateTime.now());
		book3.setReleasedAt(OffsetDateTime.now().plusDays(1));
		book1.setReleasedAt(OffsetDateTime.now().plusDays(2));
		book0.setReleasedAt(OffsetDateTime.now().plusDays(3));

		book3.setPrice(BigDecimal.valueOf(50));
		book0.setPrice(BigDecimal.valueOf(60));
		book1.setPrice(BigDecimal.valueOf(75));
		book2.setPrice(BigDecimal.valueOf(120));

		book2.setPopularity(0L);
		book1.setPopularity(25L);
		book3.setPopularity(30L);
		book0.setPopularity(35L);

		bookRepository.saveAll(Arrays.asList(book0, book1, book2, book3));

		this.graphQlTester.documentName("BookControllerTest_books")
			.variable("first", 4)
			.variable("orderBy", sort.get(order))
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
			.expect(e -> "INTERNAL".equals(e.getExtensions().get("errorType")))
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

	@Test
	void cover() throws JsonProcessingException {
		//        given
		Cover cover = (Cover.newBuilder().uuid("12").build());
		Book book = bookRepository.save(Book.newBuilder().cover(cover).build());

		this.graphQlTester.documentName("BookControllerTest_bookCover")
			.variable("id", book.getId())
			//        when
			.execute()
			//                then
			.errors()
			.verify()
			.path("node.covers[0].url")
			.entity(String.class)
			.matches(Predicate.not(String::isBlank))
			.path("node.covers[0].width")
			.valueIsNull()
			.path("node.covers[1].url")
			.entity(String.class)
			.matches(Predicate.not(String::isBlank))
			.path("node.covers[1].width")
			.entity(Integer.class)
			.isEqualTo(100);
	}
}
