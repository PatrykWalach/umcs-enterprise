package com.umcs.enterprise.book;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {

}
