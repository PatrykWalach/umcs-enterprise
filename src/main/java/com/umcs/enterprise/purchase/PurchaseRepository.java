package com.umcs.enterprise.purchase;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {}
