package com.umcs.enterprise.purchase;

import com.umcs.enterprise.node.Node;
import com.umcs.enterprise.types.PurchaseStatus;
import com.umcs.enterprise.user.User;
import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Purchase implements Node {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private UUID databaseId;

	@ManyToOne(fetch = FetchType.EAGER)
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

private 	PurchaseStatus status;

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
