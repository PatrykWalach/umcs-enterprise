package com.umcs.enterprise.basket;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookEdgeRepository extends JpaRepository<BookEdge, Long> {
    BookEdge findByBasket_User_UsernameAndBook_DatabaseId(String username, UUID databaseId);

}
