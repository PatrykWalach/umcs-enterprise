package com.umcs.enterprise.basket;

import com.umcs.enterprise.book.Book;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, UUID> {
	Basket findByUser_Username(String username);
}
