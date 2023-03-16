package com.umcs.enterprise;

import graphql.language.StringValue;
import graphql.schema.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import reactor.core.publisher.Mono;

@Configuration
public class GraphQLConfig {

	public GraphQLConfig(BatchLoaderRegistry registry, BookRepository repository) {
		registry
			.forTypePair(Long.class, Book.class)
			.registerMappedBatchLoader((ids, env) -> {
				return Mono.just(
					repository
						.findAllById(ids)
						.stream()
						.collect(Collectors.toMap(Book::getDatabaseId, Function.identity()))
				);
			});
		// more registrations ...
	}

	@Bean
	public RuntimeWiringConfigurer runtimeWiringConfigurer() {
		GraphQLScalarType date = GraphQLScalarType
			.newScalar()
			.name("Date")
			.coercing(
				new Coercing<>() {
					@Override
					public Object serialize(Object dataFetcherResult) throws CoercingSerializeException {
						if (dataFetcherResult instanceof ZonedDateTime) {
							return ((ZonedDateTime) dataFetcherResult).format(
									DateTimeFormatter.ISO_OFFSET_DATE_TIME
								);
						}
						throw new CoercingSerializeException(
							"GraphQL Date Scalar serializer expected a `ZonedDateTime` object"
						);
					}

					@Override
					public Object parseValue(Object input) throws CoercingParseValueException {
						if (input instanceof String) {
							return ZonedDateTime.parse((String) input, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
						}
						throw new CoercingParseValueException(
							"GraphQL Date Scalar parser expected a `String` object"
						);
					}

					@Override
					public Object parseLiteral(Object input) throws CoercingParseLiteralException {
						if (input instanceof StringValue) {
							return ZonedDateTime.parse(
								((StringValue) input).getValue(),
								DateTimeFormatter.ISO_OFFSET_DATE_TIME
							);
						}
						throw new CoercingParseLiteralException(
							"GraphQL Date Scalar parser expected a ast `StringValue` object"
						);
					}
				}
			)
			.build();

		return t -> t.scalar(date);
	}
}
