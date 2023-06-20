package com.umcs.enterprise.basket;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class SummableService {

	public Long sumPrice(Collection<? extends SummableEdge> books) {
		return books.stream().filter(Objects::nonNull).mapToLong(SummableEdge::getPrice).sum();
	}

	public Integer sumQuantity(Collection<? extends SummableEdge> books) {
		return books.stream().mapToInt(SummableEdge::getQuantity).sum();
	}
}
