package com.umcs.enterprise.author;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@RepositoryRestResource(excerptProjection = AuthorRepository.class)
public interface AuthorRepository extends PagingAndSortingRepository <Author, UUID>{

}
