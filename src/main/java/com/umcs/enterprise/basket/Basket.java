package com.umcs.enterprise.basket;

import com.umcs.enterprise.node.Node;
import com.umcs.enterprise.user.User;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder(builderMethodName = "newBuilder")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Basket implements Node {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private UUID databaseId;

	@OneToOne(mappedBy = "basket")
	@CreatedBy
	private User user;


	@OneToMany(fetch = FetchType.LAZY, mappedBy = "basket", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BookEdge> books;
}
