package com.umcs.enterprise.basket;

import com.umcs.enterprise.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BasketRepository extends JpaRepository<Basket, UUID> {
    Basket findByUser_Username(String username);

}
