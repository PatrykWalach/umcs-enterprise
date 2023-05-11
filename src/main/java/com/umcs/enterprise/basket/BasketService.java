package com.umcs.enterprise.basket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umcs.enterprise.auth.JwtService;
import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.purchase.BookPurchase;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


public interface BasketService {

	@NonNull Basket getBasket( ) throws JsonProcessingException;

	@NonNull String basketBook( @NonNull UUID databaseId) throws JsonProcessingException;

	@NonNull String unbasketBook( @NonNull UUID databaseId) throws JsonProcessingException;
}
