package com.umcs.enterprise.basket;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.umcs.enterprise.ConnectionService;
import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.book.BookDataLoader;
import graphql.relay.Connection;
import graphql.relay.DefaultConnection;
import graphql.relay.Edge;
import graphql.schema.DataFetchingEnvironment;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;

@DgsComponent
@RequiredArgsConstructor
public class BasketBooksDataFetcher {

	@NonNull
	private final ConnectionService connectionService;

	@DgsData(parentType = "BasketBooksEdge")
	public BigDecimal price(DataFetchingEnvironment env) {
		var edge = env.<BasketBooksEdge>getSource();

		return edge.getNode().getPrice().multiply(BigDecimal.valueOf(edge.getQuantity()));
	}

	@DgsData(parentType = "Basket")
	public CompletableFuture<Connection<Book>> books(DgsDataFetchingEnvironment env) {
		DataLoader<Long, Book> dataLoader = env.getDataLoader(BookDataLoader.class);

		Map<Long, Integer> basket = env.getSource();

		return dataLoader
			.loadMany(basket.keySet().stream().toList())
			.thenApply(books -> connectionService.getConnection(books, env))
			.thenApply(connection ->
				new DefaultConnection<>(
					connection
						.getEdges()
						.stream()
						.map(edge -> {
							Edge<Book> basketBookEdge = new BasketBooksEdge(
								edge.getCursor(),
								edge.getNode(),
								basket.get(edge.getNode().getDatabaseId())
							);

							return basketBookEdge;
						})
						.toList(),
					connection.getPageInfo()
				)
			);
	}
}
