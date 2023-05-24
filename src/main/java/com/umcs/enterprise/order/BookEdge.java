package com.umcs.enterprise.order;

import com.umcs.enterprise.book.Book;
import javax.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.userdetails.User;

@Getter
@ToString
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class BookEdge {

	private Integer quantity;

	@EmbeddedId
	private BookEdgeId id;

	@MapsId("orderId")
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@MapsId("bookId")
	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;
}
