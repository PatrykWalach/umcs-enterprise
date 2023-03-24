package com.umcs.enterprise;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import lombok.*;
import org.hibernate.Hibernate;

@Entity
@Table(name = "\"order\"")
@Getter
@Setter
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Node {

	@Id
	@GeneratedValue
	private Long databaseId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
	private List<BookOrder> books;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Order order = (Order) o;
		return getDatabaseId() != null && Objects.equals(getDatabaseId(), order.getDatabaseId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
