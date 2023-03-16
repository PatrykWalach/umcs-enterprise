package com.umcs.enterprise;


import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Map;

@Controller
public class BookController {

    //    private final AuthorRepository authors;
    private final BookRepository books;
    private final BookService bookService;

    public BookController(BookRepository books, BookService bookService) {

        this.books = books;
        this.bookService = bookService;
    }

//    @BatchMapping
//    public Map<Book, Author> author(List<Book> books) {
//
//        var authors = this.authors.findAllById(books.stream().map(Book::getAuthorId).toList()).stream().collect(Collectors.toMap(
//                Author::getId
//                , Function.identity()));
//
//        return books.stream().collect(Collectors.toMap(Function.identity(),
//                book -> authors.get(book.getAuthorId())
//        ));
//
//
//    }

    @QueryMapping
    public Connection<Book> books(@Arguments Map<String, Object> args) {

        return new Connection<>(this.books::findAll, this.books::count, args);
    }


    @SchemaMapping(typeName = "BookConnection")
    public <T>
    Connection<T> pageInfo(Connection<T> connection) {
        return connection;
    }

    @SchemaMapping(typeName = "BookConnection")
    public <T>
    Collection<Edge<T>> edges(Connection<T> connection) {
        return connection.getEdges();
    }


    @MutationMapping
    public Book createBook(@Argument CreateBookInput input) {
        var book = new Book();
        book.setAuthor(input.author());
        book.setCreatedAt(ZonedDateTime.now());
        book.setPrice(input.price());
//        book.setCoverId();
        book.setTitle(input.title());
        book.setPopularity(0);
        book.setCreatedAt(ZonedDateTime.now());


        return bookService.save(book);
    }


}
