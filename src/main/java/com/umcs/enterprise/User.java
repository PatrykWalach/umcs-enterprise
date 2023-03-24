package com.umcs.enterprise;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.List;
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
	@GeneratedValue
	private Long databaseId;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<Order> orders;

	private String password;

	@Column(unique = true)
	private String username;

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
