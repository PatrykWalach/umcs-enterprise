package com.umcs.enterprise;

import com.umcs.enterprise.basket.BasketRepository;
import com.umcs.enterprise.basket.BookEdgeRepository;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.cover.CoverRepository;
import com.umcs.enterprise.purchase.BookPurchaseRepository;
import com.umcs.enterprise.purchase.PurchaseRepository;
import com.umcs.enterprise.user.UserRepository;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class CleanDb implements BeforeEachCallback {

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {

		SpringExtension
			.getApplicationContext(context)
			.getBean(BookPurchaseRepository.class)
			.deleteAll();
		SpringExtension.getApplicationContext(context).getBean(PurchaseRepository.class).deleteAll();

		SpringExtension.getApplicationContext(context).getBean(UserRepository.class).deleteAll();

		SpringExtension
				.getApplicationContext(context)
				.getBean(BookEdgeRepository.class)
				.deleteAll();
		SpringExtension.getApplicationContext(context).getBean(BasketRepository.class).deleteAll();

		SpringExtension.getApplicationContext(context).getBean(BookRepository.class).deleteAll();
		SpringExtension.getApplicationContext(context).getBean(CoverRepository.class).deleteAll();
	}
}
