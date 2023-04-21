package com.umcs.enterprise.basket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umcs.enterprise.auth.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasketService {

@NonNull
    private final JwtService jwtService;



    public String setBasket(String Authorization, Map<UUID, Integer> basket) throws JsonProcessingException {

        JwtBuilder builder = setBasket(Jwts.builder(), basket);

        builder.setExpiration(Date.from(Instant.now().plusSeconds(60 * 60 * 24 * 365)));

        if(Authorization ==null){

            return  jwtService.signToken(
                    builder
            );


        }

        Claims jwt = jwtService.parseAuthorizationHeader(Authorization);


        if(jwt.getSubject() != null){
            builder .setSubject(jwt.getSubject());
            builder .setExpiration(jwt.getExpiration());
        }




        return  jwtService.signToken(
             builder
        );

    }

    public JwtBuilder setBasket(JwtBuilder builder, Map<UUID, Integer> basket) throws JsonProcessingException {
        return builder.setClaims(Map.of("http://localhost:8080/graphql/basket", new ObjectMapper().writeValueAsString(basket)));
    }

    public Map<UUID, Integer> getBasket(String Authorization) throws JsonProcessingException {
        if (Authorization == null) {
            return new HashMap<>();
        }



        try {




            String basket = jwtService.parseAuthorizationHeader(Authorization)
                    .get("http://localhost:8080/graphql/basket", String.class);

            if (basket == null) {
                return new HashMap<>();
            }

            return new ObjectMapper().readValue(basket, new TypeReference<>() {});
        } catch (IllegalArgumentException e) {
            return new HashMap<>();
        }
    }
}
