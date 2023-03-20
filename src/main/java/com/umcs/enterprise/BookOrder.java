package com.umcs.enterprise;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class BookOrder {

	@Id
	@GeneratedValue
	@Column(name = "database_id")
	private Long databaseId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id")
	private Book book;
}
