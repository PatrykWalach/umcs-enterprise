package com.umcs.enterprise.basket;

import com.umcs.enterprise.book.Book;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class BookEdge {

	@Id
	@GeneratedValue
	@Column(name = "database_id")
	private Long databaseId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "basket_id")
	private Basket basket;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id")
	private Book book;

	private Integer quantity;
}
