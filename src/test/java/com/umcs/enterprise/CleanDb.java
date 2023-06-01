package com.umcs.enterprise;

import com.cloudinary.Cloudinary;
import com.umcs.enterprise.basket.BasketRepository;
import com.umcs.enterprise.basket.BookEdgeRepository;
import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.book.BookRepository;

import com.umcs.enterprise.cover.Cover;
import com.umcs.enterprise.purchase.BookPurchaseRepository;
import com.umcs.enterprise.purchase.PurchaseRepository;
import com.umcs.enterprise.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CleanDb implements BeforeEachCallback {

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		SpringExtension
			.getApplicationContext(context)
			.getBean(BookPurchaseRepository.class)
			.deleteAll();
		SpringExtension.getApplicationContext(context).getBean(PurchaseRepository.class).deleteAll();

		SpringExtension.getApplicationContext(context).getBean(UserRepository.class).deleteAll();

		SpringExtension.getApplicationContext(context).getBean(BookEdgeRepository.class).deleteAll();
		SpringExtension.getApplicationContext(context).getBean(BasketRepository.class).deleteAll();

		SpringExtension.getApplicationContext(context).getBean(BookRepository.class).deleteAll();

	}


	@AfterEach
	void afterEach(ExtensionContext context) throws Exception {

		List<String> ids = SpringExtension.getApplicationContext(context).getBean(BookRepository.class)
				.findAll()
				.stream()
				.map(Book::getCover)
				.filter(Objects::nonNull)
				.map(Cover::getUuid)
				.filter(Objects::nonNull)
				.toList();

		if (ids.isEmpty()) {
			return;
		}

		cloudinary.api().deleteResources(ids, Map.of());
	}

	@Autowired
	private Cloudinary cloudinary;
}
