package com.umcs.enterprise.purchase;

import com.umcs.enterprise.types.PurchaseStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
	List<Purchase> findAllByUserDatabaseIdOrderByCreatedAtDesc(Long databaseId);

	List<Purchase> findAllByUserDatabaseIdAndStatusOrderByCreatedAtDesc(
		Long databaseId,
		PurchaseStatus status
	);
}
