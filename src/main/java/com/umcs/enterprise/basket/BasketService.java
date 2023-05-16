package com.umcs.enterprise.basket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.umcs.enterprise.types.Token;
import io.jsonwebtoken.*;
import java.util.*;
import lombok.NonNull;

public interface BasketService {
	@NonNull
	Basket getBasket() throws JsonProcessingException;

	Token basketBook(@NonNull UUID databaseId) throws JsonProcessingException;

	Token unbasketBook(@NonNull UUID databaseId) throws JsonProcessingException;
}
