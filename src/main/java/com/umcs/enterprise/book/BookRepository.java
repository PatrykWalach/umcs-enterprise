package com.umcs.enterprise.book;

import java.util.UUID;
import java.util.UUID;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(excerptProjection = BookRepository.class)
public interface BookRepository extends PagingAndSortingRepository<Book, UUID> {}
