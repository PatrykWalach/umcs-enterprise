package com.umcs.enterprise.basket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umcs.enterprise.auth.JwtService;
import com.umcs.enterprise.book.BookRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@AllArgsConstructor
public class AnonymousBasketService implements BasketService{

    @NonNull
    private final JwtService jwtService;


    private   String Authorization;

    private String setBasket(Map<UUID, Integer> basket)
            throws JsonProcessingException {

        JwtBuilder builder = (Jwts.builder().setClaims(
                Map.of("http://localhost:8080/graphql/basket", new ObjectMapper().writeValueAsString(basket))
        ))
                .setExpiration(Date.from(Instant.now().plusSeconds(60 * 60 * 24 * 365)));

        String token  =  jwtService.signToken(builder);
        this.Authorization = "Bearer "+ token;

        return token;
    }




    @NonNull
    private final BookRepository bookRepository;

    @Override
    @NonNull public Basket getBasket() throws JsonProcessingException {
        if (Authorization == null) {
            return Basket.newBuilder().books(new ArrayList<>()).build();
        }

        try {
            Claims token = jwtService
                    .parseAuthorizationHeader(Authorization);




            String basket =				token.get("http://localhost:8080/graphql/basket", String.class);

            if (basket == null) {
                return Basket.newBuilder().books(new ArrayList<>()).build();
            }

            Map<UUID, Integer> parsed = new ObjectMapper().readValue(basket, new TypeReference<>() {});



            return Basket.newBuilder().books(
                    bookRepository
                            .findAllById(parsed.keySet())
                            .stream()
                            .map((e)->   BookEdge.newBuilder().book(e).quantity(parsed.get(e.getDatabaseId())).build()      )
                            .collect(Collectors.toList())
            ).build();

        } catch (IllegalArgumentException e) {
            return Basket.newBuilder().books(new ArrayList<>()).build();
        }
    }

    @Override
    public @NonNull String basketBook( @NonNull UUID databaseId) throws JsonProcessingException {
        Basket basket = getBasket();
        Map<UUID, Integer> books = basket.getBooks().stream().collect(Collectors.toMap(e -> e.getBook().getDatabaseId(), BookEdge::getQuantity));
        books.merge(databaseId, 1, Integer::sum);
        return setBasket(books);
    }

    @Override
    public @NonNull String unbasketBook(@NonNull UUID databaseId) throws JsonProcessingException {
        Basket basket = getBasket();
        Map<UUID, Integer> books = basket.getBooks().stream().collect(Collectors.toMap(e -> e.getBook().getDatabaseId(), BookEdge::getQuantity));

        books.computeIfPresent(databaseId, (key,value)->{
            if(value < 2){
                return  null;
            }
            return value-1;
        });

        return setBasket( books);
    }
}
