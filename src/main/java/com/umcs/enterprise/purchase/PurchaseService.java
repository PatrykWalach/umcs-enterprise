package com.umcs.enterprise.purchase;

import com.umcs.enterprise.types.PurchaseStatus;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchaseService {

	private final PurchaseRepository purchaseRepository;

	public List<Purchase> findByUserDatabaseId(Long databaseId) {
		return purchaseRepository.findAllByUserDatabaseId(databaseId);
	}

	public List<Purchase> findByUserDatabaseId(Long databaseId, PurchaseStatus status) {
		return purchaseRepository.findAllByUserDatabaseIdAndStatus(databaseId, status);
	}

	@PostFilter("hasRole('ADMIN') or filterObject.user.username == authentication.name")
	public List<Purchase> findAllById(Iterable<Long> ids) {
		return purchaseRepository.findAllById(ids);
	}

	public Purchase save(Purchase entity) {
		return purchaseRepository.save(entity);
	}

	public List<Purchase> saveAll(List<Purchase> entities) {
		return purchaseRepository.saveAll(entities);
	}
}
