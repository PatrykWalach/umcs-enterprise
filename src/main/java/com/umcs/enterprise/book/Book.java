package com.umcs.enterprise.book;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.umcs.enterprise.author.Author;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@ToString
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	@Type(type = "uuid-char")
	private UUID id;

	@JsonIgnore
	@LastModifiedDate
	private LocalDateTime lastModifiedDate;

	@Version
	private Long version;

	@LastModifiedDate
	private LocalDateTime releaseDate;

	//    @NotEmpty
	@Setter
	private String title;

	@ToString.Exclude
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
		name = "Book_authors",
		joinColumns = @JoinColumn(name = "book_id"),
		inverseJoinColumns = @JoinColumn(name = "authors_id")
	)
	@Builder.Default
	private Set<Author> authors = new LinkedHashSet<>();

	@Embedded
	private Covers covers;

	@Column(length = 1020)
	private String synopsis;

	private BigDecimal price;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Book book = (Book) o;
		return getId() != null && Objects.equals(getId(), book.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
