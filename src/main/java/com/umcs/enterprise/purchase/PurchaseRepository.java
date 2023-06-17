package com.umcs.enterprise.purchase;

import com.umcs.enterprise.types.PurchaseStatus;
import java.awt.*;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
	List<Purchase> findAllByUserDatabaseId(Long databaseId);

	List<Purchase> findAllByUserDatabaseIdAndStatus(Long databaseId, PurchaseStatus status);
}
