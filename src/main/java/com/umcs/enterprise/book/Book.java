package com.umcs.enterprise.book;

import com.umcs.enterprise.cover.Cover;
import com.umcs.enterprise.node.Node;
import com.umcs.enterprise.purchase.BookPurchase;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder(builderMethodName = "newBuilder")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Book implements Node {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private UUID databaseId;

	private String author;

	private String title;

	@Column(length = 2_000)
	private String synopsis;

	@OneToOne( cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "cover_id", nullable = false)
	@Builder.Default
	private Cover cover = new Cover();

	@Column(precision = 19, scale = 4)
	private BigDecimal price;

	@CreatedDate
	@Column(nullable = false)
	private Instant createdAt;

	private OffsetDateTime releasedAt;

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
