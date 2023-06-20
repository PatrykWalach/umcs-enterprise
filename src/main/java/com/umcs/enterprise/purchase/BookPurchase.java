package com.umcs.enterprise.purchase;

import com.umcs.enterprise.basket.SummableEdge;
import com.umcs.enterprise.book.Book;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class BookPurchase implements SummableEdge {

	@EmbeddedId
	private BookPurchaseId databaseId;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("purchaseId")
	@JoinColumn(name = "purchase_id")
	private Purchase purchase;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "book_id")
	@MapsId("bookId")
	private Book book;

	@Column(nullable = false)
	@Builder.Default
	private Integer quantity = 1;
}
