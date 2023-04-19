package com.umcs.enterprise.user;

import java.util.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

	@NonNull
	private final UserRepository userRepository;

	@NonNull
	private final PasswordEncoder passwordEncoder;

	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public User save(User user) {
		Optional
			.ofNullable(user.getPassword())
			.map(passwordEncoder::encode)
			.ifPresent(user::setPassword);

		return userRepository.save(user);
	}

	@PostFilter("hasRole('ADMIN') or filterObject.username == authentication.principal.username")
	public List<User> findAllById(Iterable<UUID> ids) {
		return userRepository.findAllById(ids);
	}
}
