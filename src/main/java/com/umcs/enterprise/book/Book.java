package com.umcs.enterprise.book;

import com.umcs.enterprise.cover.Cover;
import com.umcs.enterprise.node.Node;
import com.umcs.enterprise.purchase.BookPurchase;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import lombok.*;
import org.hibernate.Hibernate;

@Entity
@Builder(builderMethodName = "newBuilder")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Node {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "database_id", nullable = false)
	private Long databaseId;

	private String author;

	private String title;

	@Column(length = 2_000)
	private String synopsis;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cover_id", nullable = false)
	private Cover cover;

	@Column(precision = 19, scale = 4)
	private BigDecimal price;

	private ZonedDateTime createdAt;

	private ZonedDateTime releasedAt;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
	private List<BookPurchase> purchases;

	private Long popularity;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Book book = (Book) o;
		return getDatabaseId() != null && Objects.equals(getDatabaseId(), book.getDatabaseId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
