package com.umcs.enterprise.purchase;

import com.umcs.enterprise.types.PurchaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
	List<Purchase> findAllByUserDatabaseIdOrderByCreatedAtDesc(Long databaseId);

	List<Purchase> findAllByUserDatabaseIdAndStatusOrderByCreatedAtDesc(Long databaseId, PurchaseStatus status);
}
