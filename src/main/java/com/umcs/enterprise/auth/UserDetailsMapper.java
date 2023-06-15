package com.umcs.enterprise.auth;

import com.umcs.enterprise.user.User;
import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Mapper
public interface UserDetailsMapper {
	@Mapping(target = "enabled", ignore = true)
	@Mapping(target = "credentialsNonExpired", ignore = true)
	@Mapping(target = "accountNonLocked", ignore = true)
	@Mapping(target = "accountNonExpired", ignore = true)
	UserDetailsDto userToUserDetails(User user);

	default Collection<? extends SimpleGrantedAuthority> getAuthorities(List<String> authorities) {
		return authorities.stream().map(SimpleGrantedAuthority::new).toList();
	}
}
