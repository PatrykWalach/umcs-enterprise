package com.umcs.enterprise.purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookPurchaseRepository extends JpaRepository<BookPurchase, Long> {}
