package com.umcs.enterprise;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(classes = EnterpriseApplication.class)
@ExtendWith(CleanDb.class)
class HelloDataFetcherTest {

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
