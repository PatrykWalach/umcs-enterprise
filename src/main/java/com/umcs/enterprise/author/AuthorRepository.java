package com.umcs.enterprise.author;

import java.util.UUID;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(excerptProjection = AuthorRepository.class)
public interface AuthorRepository extends PagingAndSortingRepository <Author, UUID>{

}
