package com.umcs.enterprise;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.cover.Cover;
import com.umcs.enterprise.cover.CoverRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = EnterpriseApplication.class)
class NodeDataFetcherTest {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private GraphQlTester graphQlTester;

	@Autowired
	private CoverRepository coverRepository;

	@Test
	void returnsNode() {
		//        given
		var cover = coverRepository.save(new Cover());
		var book = bookRepository.save(Book.newBuilder().cover(cover).title("Book title").build());

		this.graphQlTester.documentName("NodeControllerTest_returnsNode")
			.variable("id", book.getId())
			//        when
			.execute()
			//                then
			.errors()
			.verify()
			.path("node.id")
			.entity(String.class)
			.isEqualTo(book.getId())
			.path("node.title")
			.entity(String.class)
			.isEqualTo(book.getTitle());
	}
}
