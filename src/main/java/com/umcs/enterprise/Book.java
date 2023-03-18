package com.umcs.enterprise;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.ZonedDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Builder(builderMethodName ="newBuilder")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Node {




	@Id
	@GeneratedValue
	private Long databaseId;

	
	private String author;

	
	private String title;

	@Column(length = 2_000)
	private String synopsis;



	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "cover_id")
	private Cover cover;



	
	private int price;

	
	private ZonedDateTime createdAt;



	
	private ZonedDateTime releasedAt;

	
	private Long popularity;

	@ManyToMany(mappedBy = "books")
	private Set<Order> orders = new LinkedHashSet<>();

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
