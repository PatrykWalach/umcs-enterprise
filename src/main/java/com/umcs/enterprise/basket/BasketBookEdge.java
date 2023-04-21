package com.umcs.enterprise.basket;

import com.umcs.enterprise.book.Book;
import graphql.relay.ConnectionCursor;
import graphql.relay.Edge;
import lombok.*;

@Getter
@Builder(builderMethodName = "newBuilder")
@AllArgsConstructor
@NoArgsConstructor
public class BasketBookEdge implements Edge<Book> {

	private ConnectionCursor cursor;
	private Book node;
	private Integer quantity;
}
