package com.umcs.enterprise.purchase;

import com.umcs.enterprise.relay.node.Node;
import com.umcs.enterprise.user.User;
import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedBy;

@Entity
@Getter
@Setter
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class Purchase implements Node {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private UUID databaseId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@CreatedBy
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
