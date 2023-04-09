package com.umcs.enterprise;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = EnterpriseApplication.class)
@ExtendWith(CleanDb.class)
class HelloDataFetcherTest {

	@Autowired
	private HttpGraphQlTester graphQlTester;

	@Test
	void returnsHello() {
		//        given
		this.graphQlTester.documentName("HelloControllerTest_returnsHello")
			//                when
			.execute()
			//                then
			.errors()
			.verify()
			.path("hello")
			.matchesJson("\"Hello World!\"");
	}
}
