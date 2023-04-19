package com.umcs.enterprise.purchase;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {}
