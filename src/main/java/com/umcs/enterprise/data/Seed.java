package com.umcs.enterprise.data;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umcs.enterprise.author.AuthorRepository;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.book.cover.Covers;
import java.io.IOException;
import java.util.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

@Configuration
@RequiredArgsConstructor
public class Seed {

	@NonNull
	private final BookRepository bookRepository;

	@NonNull
	private final AuthorRepository authorRepository;

	@NonNull
	private final Cloudinary cloudinary;

	@Bean
	public Jackson2RepositoryPopulatorFactoryBean jackson2RepositoryPopulatorFactoryBean()
		throws Exception {
		Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
		factory.setResources(new Resource[] { new ClassPathResource("data.json") });
		factory.afterPropertiesSet();
		return factory;
	}

	private Covers upload(String url, String uuid) {
		Map<?, ?> result;

		try {
			result =
				cloudinary
					.uploader()
					.upload(
						url,
						Map.of(
							"public_id",
							uuid,
							"overwrite",
							false,
							//                    "notification_url", "https://mysite.com/webhook_endpoint"
							"responsive_breakpoints",
							Map.of(
								"create_derived",
								true,
								"bytes_step",
								20_000,
								"min_width",
								100,
								"max_width",
								2000,
								"transformation",
								new Transformation<>()
									.aspectRatio("3:4")
									.crop("fill_pad")
									.gravity("auto")
									.quality("auto")
									.fetchFormat("auto")
									.generate()
							)
						)
					);
		} catch (IOException e) {
			return null;
		}

		CoversMapper.ResultDto resultDto = new ObjectMapper()
			.convertValue(result, CoversMapper.ResultDto.class);

		authorRepository
			.saveAll(List.of(Author.builder().name("Author").build()))
			.forEach(authors::add);

		return Mappers
			.getMapper(CoversMapper.class)
			.breakpointsToCovers(resultDto.responsive_breakpoints().get(0));
	}
}
