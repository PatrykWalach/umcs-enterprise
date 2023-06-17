package com.umcs.enterprise.user;

import java.util.Optional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAuditorAware implements AuditorAware<User> {
@NonNull
	private final UserRepository userRepository;




	@Override
	public Optional<User> getCurrentAuditor() {
		return Optional
			.ofNullable(SecurityContextHolder.getContext().getAuthentication())
			.filter(Authentication::isAuthenticated)
			.map(Authentication::getName)
			.flatMap(userRepository::findByUsername);
	}
}
