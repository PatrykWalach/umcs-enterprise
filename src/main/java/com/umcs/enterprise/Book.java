package com.umcs.enterprise;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.*;
import org.hibernate.Hibernate;

@Entity
@Builder(builderMethodName = "newBuilder")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SecondaryTable(
	name = "book_summary",
	pkJoinColumns = {
		@PrimaryKeyJoinColumn(name = "database_id", referencedColumnName = "databaseId")
	},
	foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
)
public class Book implements Node {

	@Id
	@GeneratedValue
	private Long databaseId;

	private String author;

	private String title;

	@Column(length = 2_000)
	private String synopsis;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cover_id")
	private Cover cover;

	@Column(precision = 19, scale = 4)
	private BigDecimal price;

	private ZonedDateTime createdAt;

	private ZonedDateTime releasedAt;

	@Column(table = "book_summary")
	private Integer popularity;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
	private List<BookOrder> orders;

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
