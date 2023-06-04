package com.umcs.enterprise;

import com.cloudinary.Cloudinary;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.cover.Cover;
import com.umcs.enterprise.cover.CoverService;
import com.umcs.enterprise.purchase.*;
import com.umcs.enterprise.user.User;
import com.umcs.enterprise.user.UserService;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

@Configuration
@EnableJpaAuditing
@RequiredArgsConstructor
public class Seed {

	@NonNull
	private final PurchaseService purchaseRepository;

	@NonNull
	private final BookPurchaseRepository bookPurchaseRepository;

	@NonNull
	private final UserService userRepository;

	private Cover toCover(String url, String uuid) {
		//		try {
		//			cloudinary.uploader().upload(url, Map.of("public_id", uuid));
		//		} catch (IOException e) {
		//			return null;
		//		}
		return (Cover.newBuilder().uuid(uuid).url(url).build());
	}

	@NonNull
	private Cloudinary cloudinary;

	@Bean
	public Jackson2RepositoryPopulatorFactoryBean jackson2RepositoryPopulatorFactoryBean(
		ObjectMapper mapper
	) throws Exception {
		Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
		factory.setResources(new Resource[] { new ClassPathResource("books.json") });
		//		factory.afterPropertiesSet();
		factory.setMapper(mapper);

		return factory;
	}
}
