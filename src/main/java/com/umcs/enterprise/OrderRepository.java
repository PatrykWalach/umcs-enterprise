package com.umcs.enterprise;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
	long countByBooks_DatabaseId(Long databaseId);
}
