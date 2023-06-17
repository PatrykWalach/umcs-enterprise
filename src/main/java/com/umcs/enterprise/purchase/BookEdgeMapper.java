package com.umcs.enterprise.purchase;

import com.umcs.enterprise.basket.BookEdge;
import com.umcs.enterprise.book.Book;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(builder = @Builder(disableBuilder = true))
public interface BookEdgeMapper {
	@Mapping(target = "databaseId.purchaseId", source = "purchase.databaseId")
	@Mapping(target = "databaseId.bookId", source = "edge.book.databaseId")
	BookPurchase bookEdgeToBookPurchase(BookEdge edge, Purchase purchase);
}
