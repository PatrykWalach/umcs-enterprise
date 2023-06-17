package com.umcs.enterprise.user;

import jakarta.transaction.Transactional;
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

	@Transactional
	public User save(User user) {
		if (user.getPassword() != null) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}

		return userRepository.save(user);
	}

	@PostFilter("hasRole('ADMIN') or filterObject.username == authentication.name")
	public List<User> findAllById(Iterable<Long> ids) {
		return userRepository.findAllById(ids);
	}

	public Long count() {
		return userRepository.count();
	}
}
