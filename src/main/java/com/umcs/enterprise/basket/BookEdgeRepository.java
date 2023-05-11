package com.umcs.enterprise.basket;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookEdgeRepository extends JpaRepository<BookEdge, Long> {
	BookEdge findByBasket_User_UsernameAndBook_DatabaseId(String username, UUID databaseId);
}
