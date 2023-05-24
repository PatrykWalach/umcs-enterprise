package com.umcs.enterprise.book;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.UUID;



@RepositoryRestResource(excerptProjection = BookRepository.class)
public interface BookRepository extends PagingAndSortingRepository<Book, UUID> {

}
