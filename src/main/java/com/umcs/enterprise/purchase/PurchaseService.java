package com.umcs.enterprise.purchase;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchaseService {

	private final PurchaseRepository purchaseRepository;

	@PostFilter(
		"hasRole('ADMIN') or filterObject.user.databaseId == authentication.principal.databaseId"
	)
	public List<Purchase> findAllById(Iterable<UUID> ids) {
		return purchaseRepository.findAllById(ids);
	}

	public Purchase save(Purchase entity) {
		return purchaseRepository.save(entity);
	}

	public List<Purchase> saveAll(List<Purchase> entities) {
		return purchaseRepository.saveAll(entities);
	}
}
