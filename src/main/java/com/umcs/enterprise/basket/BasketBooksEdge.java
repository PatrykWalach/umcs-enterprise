package com.umcs.enterprise.basket;

import com.umcs.enterprise.ConnectionService;
import com.umcs.enterprise.book.Book;
import graphql.relay.ConnectionCursor;
import graphql.relay.Edge;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import lombok.*;
import org.springframework.data.domain.Sort;

@Getter
@Builder(builderMethodName = "newBuilder")
@AllArgsConstructor
@NoArgsConstructor
public class BasketBooksEdge implements Edge<Book> {

	private ConnectionCursor cursor;
	private Book node;
	private Integer quantity;
}
