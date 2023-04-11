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

	@Id
	@GeneratedValue
	@Column(name = "database_id")
	private Long databaseId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "purchase_id")
	private Purchase purchase;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id")
	private Book book;

	private Integer quantity;
}
