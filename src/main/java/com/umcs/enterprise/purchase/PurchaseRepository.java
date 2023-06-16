package com.umcs.enterprise.purchase;

import java.awt.*;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {

    Window<Purchase> findAll(Pageable pageable, OffsetScrollPosition position);
}
