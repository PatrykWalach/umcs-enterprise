package com.umcs.enterprise.purchase;

import com.umcs.enterprise.book.Book;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class BookPurchase {

	@EmbeddedId
	private BookPurchaseId databaseId;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("purchaseId")
	@JoinColumn(name = "purchase_id")
	private Purchase purchase;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id")
	@MapsId("bookId")
	private Book book;

	private Integer quantity;
}
