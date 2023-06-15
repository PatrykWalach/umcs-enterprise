package com.umcs.enterprise.user;

import com.umcs.enterprise.node.Node;
import com.umcs.enterprise.purchase.Purchase;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import lombok.*;

@Entity(name = "\"user\"")
@Getter
@Setter
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class User implements Node {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private UUID databaseId;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<Purchase> purchases;

	private String password;

	@Column(unique = true)
	private String username;

	private List<String> authorities;
}
