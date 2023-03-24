package com.umcs.enterprise;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService
	implements org.springframework.security.core.userdetails.UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) {
		return userRepository
			.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username)
			);
	}
}
