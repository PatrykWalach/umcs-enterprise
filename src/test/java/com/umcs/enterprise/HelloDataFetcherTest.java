package com.umcs.enterprise;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
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
