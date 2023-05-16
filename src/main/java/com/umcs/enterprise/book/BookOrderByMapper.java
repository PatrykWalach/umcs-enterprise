package com.umcs.enterprise.book;

import com.umcs.enterprise.types.BookOrderBy;
import java.util.*;
import org.mapstruct.*;
import org.springframework.data.domain.Sort;

@Mapper
public interface BookOrderByMapper {
	Sort.Direction orderToDirection(com.umcs.enterprise.types.Order order);

	List<Sort.Order> bookOrderByListToOrders(List<BookOrderBy> orderBy);

	@Mapping(target = "price", source = "price.raw")
	BookOrderByDTO bookOrderByToBookOrderByDTO(BookOrderBy orderBy);

	default Sort.Order bookOrderByToSort(BookOrderByDTO orderBy) {
		HashMap<String, Sort.Direction> map = new HashMap<>();

		map.put("popularity", orderBy.popularity());
		map.put("price", orderBy.price());
		map.put("releasedAt", orderBy.releasedAt());

		System.out.println(map);

		return map
			.entrySet()
			.stream()
			.filter(e -> Objects.nonNull(e.getValue()))
			.findAny()
			.map(entry -> new Sort.Order(entry.getValue(), entry.getKey()))
			.orElse(null);
	}

	record BookOrderByDTO(
		Sort.Direction popularity,
		Sort.Direction price,
		Sort.Direction releasedAt
	) {}
}
