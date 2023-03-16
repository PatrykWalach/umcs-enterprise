package com.umcs.enterprise;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@SpringBootTest(webEnvironment = RANDOM_PORT, classes = EnterpriseApplication.class)
class HelloDataFetcherTest {
    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    void returnsHello() {
//        given
        this.graphQlTester
                .documentName("HelloControllerTest_returnsHello")
//                when
                .execute()
                //                then
                .errors().verify()
                .path("hello")
                .matchesJson("\"Hello World!\"");
    }
}