package com.umcs.enterprise;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class BookService {


    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    @PreAuthorize("hasRole('ADMIN')")

    public <S extends Book> S save(S entity) {
        return repository.save(entity);
    }
}
