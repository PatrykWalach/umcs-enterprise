package com.umcs.enterprise.purchase;

import java.awt.*;
import java.util.List;
import java.util.UUID;

import com.umcs.enterprise.types.PurchaseStatus;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {



	List<Purchase> findAllByUserDatabaseId(UUID databaseId);

	List<Purchase> findAllByUserDatabaseIdAndStatus(UUID databaseId, PurchaseStatus status);
}
