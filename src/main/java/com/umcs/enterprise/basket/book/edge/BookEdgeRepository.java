package com.umcs.enterprise.basket.book.edge;

import java.util.UUID;

import com.umcs.enterprise.basket.book.edge.BookEdge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookEdgeRepository extends JpaRepository<BookEdge, Long> {
	BookEdge findByBasket_User_UsernameAndBook_DatabaseId(String username, UUID databaseId);
}
