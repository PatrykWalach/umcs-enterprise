package com.umcs.enterprise.purchase;

import com.umcs.enterprise.node.Node;
import com.umcs.enterprise.user.User;
import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import lombok.*;
import org.hibernate.Hibernate;

@Entity
@Getter
@Setter
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class Purchase implements Node {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "database_id", nullable = false)
	private Long databaseId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "purchase")
	private List<BookPurchase> books;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Purchase purchase = (Purchase) o;
		return getDatabaseId() != null && Objects.equals(getDatabaseId(), purchase.getDatabaseId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
