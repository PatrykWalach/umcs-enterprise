package com.umcs.enterprise.order;

import com.umcs.enterprise.book.Book;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;
@RepositoryRestResource
public interface OrderRepository extends PagingAndSortingRepository<Order, UUID> {
}
