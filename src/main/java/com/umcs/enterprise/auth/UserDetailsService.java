package com.umcs.enterprise.auth;

import com.umcs.enterprise.user.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService
	implements org.springframework.security.core.userdetails.UserDetailsService {

	@NonNull
	private final UserService userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) {
		return userRepository
			.findByUsername(username)
			.map(user -> Mappers.getMapper(UserDetailsMapper.class).userToUserDetails(user))
			.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username)
			);
	}
}
