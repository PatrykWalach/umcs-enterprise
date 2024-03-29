package com.umcs.enterprise.purchase;

import com.umcs.enterprise.node.Node;
import com.umcs.enterprise.types.PurchaseStatus;
import com.umcs.enterprise.user.User;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Purchase implements Node<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long databaseId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	@CreatedBy
	private User user;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "purchase")
	private Set<BookPurchase> books;

	@CreatedDate
	@Column(nullable = false)
	private Instant createdAt;

	@Column(length = 2_000)
	private String payUrl;

	private String orderId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Purchase purchase = (Purchase) o;
		return getDatabaseId() != null && Objects.equals(getDatabaseId(), purchase.getDatabaseId());
	}

	@Column(nullable = false)
	@Builder.Default
	private PurchaseStatus status = PurchaseStatus.MADE;

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
