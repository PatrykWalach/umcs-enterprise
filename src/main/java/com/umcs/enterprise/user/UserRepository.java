package com.umcs.enterprise.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {

	@Transactional(propagation = Propagation.REQUIRES_NEW)

	Optional<User> findByUsername(String username);
}
