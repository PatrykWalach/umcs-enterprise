package com.umcs.enterprise.user;

import com.umcs.enterprise.basket.Basket;
import com.umcs.enterprise.purchase.Purchase;
import com.umcs.enterprise.relay.node.Node;
import jakarta.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity(name = "\"user\"")
@Getter
@Setter
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails, Node {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private UUID databaseId;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<Purchase> purchases;

	private String password;

	@Column(unique = true)
	private String username;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "basket_id")
	private Basket basket;

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	private List<String> authorities;

	@Override
	public Collection<? extends SimpleGrantedAuthority> getAuthorities() {
		return authorities.stream().map(SimpleGrantedAuthority::new).toList();
	}
}
