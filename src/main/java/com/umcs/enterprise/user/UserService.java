package com.umcs.enterprise.user;

import com.umcs.enterprise.basket.Basket;
import com.umcs.enterprise.basket.BasketRepository;
import com.umcs.enterprise.basket.book.edge.BookEdgeRepository;
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

	@NonNull
	private final BasketRepository basketRepository;

	@NonNull
	private final BookEdgeRepository bookEdgeRepository;

	@Transactional
	public User save(User user) {
		Optional
			.ofNullable(user.getPassword())
			.map(passwordEncoder::encode)
			.ifPresent(user::setPassword);

		if (user.getBasket() == null) {
			user.setBasket(basketRepository.save(new Basket()));
		} else {
			Basket saved = basketRepository.save(user.getBasket());
			user.getBasket().getBooks().forEach(edge -> edge.setBasket(saved));
			bookEdgeRepository.saveAll(user.getBasket().getBooks());
		}

		return userRepository.save(user);
	}

	@PostFilter("hasRole('ADMIN') or filterObject.username == authentication.principal.username")
	public List<User> findAllById(Iterable<UUID> ids) {
		return userRepository.findAllById(ids);
	}
}
