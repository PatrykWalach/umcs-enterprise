package com.umcs.enterprise;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = EnterpriseApplication.class)
class NodeControllerTest {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private GraphQlTester graphQlTester;

	@Test
	void returnsNode() throws JSONException {
		//        given
		var book = new Book();
		book.setTitle("Book title");
		bookRepository.save(book);

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