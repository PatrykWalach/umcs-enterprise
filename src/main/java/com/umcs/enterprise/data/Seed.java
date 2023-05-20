package com.umcs.enterprise.data;

import com.umcs.enterprise.author.Author;
import com.umcs.enterprise.author.AuthorRepository;
import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.book.BookRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class Seed {


    @NonNull
    private  final BookRepository bookRepository;

    @NonNull
    private  final AuthorRepository authorRepository;

@Bean
    public void seedData(){



    HashSet<Author> authors = new HashSet<>();

    authorRepository.saveAll(List.of(Author.builder().name("Author").build())).forEach(authors::add);

    bookRepository.saveAll(
List.of(            Book.builder().title("Book")
        .authors(authors)
        .build())
    );
}
}
