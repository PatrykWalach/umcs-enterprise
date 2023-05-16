package com.umcs.enterprise.basket;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, UUID> {
	Basket findByUser_Username(String username);
}
