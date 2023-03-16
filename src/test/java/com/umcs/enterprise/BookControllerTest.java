package com.umcs.enterprise;

import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@SpringBootTest(webEnvironment = RANDOM_PORT, classes = EnterpriseApplication.class)
class BookControllerTest {
    @Autowired
    private GraphQlTester graphQlTester;

    @Autowired
    private BookRepository bookRepository;


    @AfterEach
    void afterEach() {
        bookRepository.deleteAll();
    }


    @Test
    void createBook_admin() throws JSONException {
        //        given
        var input = new CreateBookInput(
                new CreateBookCoverInput(""),
                "The Book",
                "The Author",
                100
        );


        this.graphQlTester.documentName("BookControllerTest_createBook").variable("input", input)
//        when
                .execute()
                //                then
                .errors().verify()
                .path("createBook.title").entity(String.class).isEqualTo(input.title())
                .path("createBook.author").entity(String.class).isEqualTo(input.author())
                .path("createBook.price").entity(String.class).isEqualTo(input.price().toString())
                .path("createBook.popularity").entity(Integer.class).isEqualTo(0)

        ;

        Assert.isTrue(bookRepository.count() == 1, "There should be one book");
    }

    @Test
    @Disabled("auth not implemented yet")
    void createBook_user() throws JSONException {
        //        given
        var input = new CreateBookInput(
                new CreateBookCoverInput(""),
                "The Book",
                "The Author",
                100
        );


        this.graphQlTester.documentName("BookControllerTest_createBook").variable("input", input)
//        when
                .execute()
                //                then
                .errors().expect(e -> Objects.requireNonNull(e.getMessage()).contains("Unauthorized"))
                .verify()
                .path("createBook").valueIsNull()

        ;

        Assert.isTrue(bookRepository.count() == 0, "There should be no books");
    }


    @ParameterizedTest
    @CsvSource({"10,5,,,true,true,6,13", "10,,,,true,false,0,9", "10,9,,,false,true,10,13", ",,10,,false,true,4,13", ",,10,10,true,false,0,9"})
    void books(Integer first, String after, Integer last, String before, boolean hasNextPage, boolean hasPreviousPage, String startCursor, String endCursor) throws JSONException {
//        given

        bookRepository.saveAll(IntStream.range(0, 14).mapToObj((i) -> new Book()).collect(Collectors.toList()));


        this.graphQlTester.documentName("BookControllerTest_books").variable("first", first).variable("before", before).variable("after", after).variable("last", last)
                //        when
                .execute()
                //                then
                .errors().verify()


                .path("books.pageInfo.startCursor").entity(String.class).isEqualTo(startCursor)
                .path("books.pageInfo.endCursor").entity(String.class).isEqualTo(endCursor)
                .path("books.pageInfo.hasNextPage").entity(Boolean.class).isEqualTo(hasNextPage)
                .path("books.pageInfo.hasPreviousPage").entity(Boolean.class).isEqualTo(hasPreviousPage)

        ;
    }


    @ParameterizedTest
    @CsvSource({"-1,,,", ",,-1,", "10,,10,", ",10,,10", "10,,10,", ",10,10,", "0,,,", ",,0,"})
    void books_variables(Integer first, String after, Integer last, String before) throws JSONException {
//        given


        this.graphQlTester.documentName("BookControllerTest_books").variable("first", first).variable("before", before).variable("after", after).variable("last", last)
                //        when
                .execute()
                //                then
                .errors().expect(e -> true).verify()
                .path("books").valueIsNull()

        ;
    }

    @Test
    void books_empty() throws JSONException {
//        given


        this.graphQlTester.documentName("BookControllerTest_books").variable("last", 10)
                //        when
                .execute()
                //                then
                .errors().verify()


                .path("books.pageInfo.hasNextPage").entity(Boolean.class).isEqualTo(false)
                .path("books.pageInfo.hasPreviousPage").entity(Boolean.class).isEqualTo(false)
                .path("books.pageInfo.startCursor").valueIsNull()
                .path("books.pageInfo.endCursor").valueIsNull()


        ;
    }
}