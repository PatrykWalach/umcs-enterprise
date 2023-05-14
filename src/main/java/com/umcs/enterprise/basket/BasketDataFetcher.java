package com.umcs.enterprise.basket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.*;
import com.netflix.graphql.dgs.internal.DgsWebMvcRequestData;
import com.umcs.enterprise.auth.JwtService;
import com.umcs.enterprise.auth.UserDetailsService;
import com.umcs.enterprise.book.BookDataLoader;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.node.GlobalId;
import com.umcs.enterprise.purchase.BookPurchase;
import com.umcs.enterprise.types.*;
import graphql.schema.DataFetchingEnvironment;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import javax.crypto.SecretKey;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.descriptor.web.ContextHandler;
import org.apache.tomcat.util.http.parser.Authorization;
import org.dataloader.DataLoader;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.context.request.ServletWebRequest;

@DgsComponent
@RequiredArgsConstructor
public class BasketDataFetcher {

	@Value("${spring.security.authentication.jwt.secret}")
	private String secret;

	@DgsQuery
	public Basket basket(@RequestHeader(required = false) String Authorization)
		throws JsonProcessingException {
		return getBasketService(Authorization).getBasket();
	}

	@NonNull
	private final JwtService jwtService;

	@NonNull
	private final BookRepository bookRepository;

	@NonNull
	private final BasketRepository basketRepository;

	@NonNull
	private final BookEdgeRepository bookEdgeRepository;

	private BasketService getBasketService(String Authorization) {
		if (
			Authorization != null &&
			jwtService.parseAuthorizationHeader(Authorization).orElseThrow().getSubject() != null
		) {
			return new UserBasketService(
				jwtService,
				basketRepository,
				bookEdgeRepository,
				Authorization,
				bookRepository
			);
		}

		return new AnonymousBasketService(jwtService, Authorization, bookRepository);
	}

	@DgsData(parentType = "Basket")
	public BigDecimal price(DgsDataFetchingEnvironment env) {
		DataLoader<UUID, com.umcs.enterprise.book.Book> dataLoader = env.getDataLoader(
			BookDataLoader.class
		);

		Basket basket = env.getSource();

		return basket
			.getBooks()
			.stream()
			.filter(Objects::nonNull)
			.map(edge -> edge.getBook().getPrice().multiply(BigDecimal.valueOf(edge.getQuantity())))
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	@DgsData(parentType = "Basket")
	public Integer quantity(DgsDataFetchingEnvironment env) {
		Basket basket = env.getSource();
		return basket.getBooks().size();
	}

	@DgsMutation
	public BasketBookResult basketBook(
		@InputArgument BasketBookInput input,
		@RequestHeader(required = false) String Authorization,
		DgsDataFetchingEnvironment dfe
	) throws JsonProcessingException {
		GlobalId globalId = GlobalId.from(input.getBook().getId());
		assert Objects.equals(globalId.className(), "Book");

		BasketService service = getBasketService(Authorization);
		String token = service.basketBook(globalId.databaseId());

		return BasketBookResult.newBuilder().token(token).basket(service.getBasket()).build();
	}

	@DgsMutation
	public UnbasketBookResult unbasketBook(
		@InputArgument UnbasketBookInput input,
		@RequestHeader(required = false) String Authorization,
		DgsDataFetchingEnvironment dfe
	) throws JsonProcessingException {
		GlobalId globalId = GlobalId.from(input.getBook().getId());
		assert Objects.equals(globalId.className(), "Book");

		BasketService service = getBasketService(Authorization);

		String token = service.unbasketBook(globalId.databaseId());

		return UnbasketBookResult.newBuilder().token(token).basket(service.getBasket()).build();
	}
}
