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
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;

@DgsComponent
@RequiredArgsConstructor
public class BasketBookDataFetcher {

	@NonNull
	private final ConnectionService connectionService;

	@DgsData(parentType = "BasketBookEdge")
	public BigDecimal price(DataFetchingEnvironment env) {
		var edge = env.<BasketBookEdge>getSource();

		return edge.getNode().getPrice().multiply(BigDecimal.valueOf(edge.getQuantity()));
	}

	@DgsData(parentType = "Basket")
	public CompletableFuture<Connection<Book>> books(DgsDataFetchingEnvironment env) {
		DataLoader<UUID, Book> dataLoader = env.getDataLoader(BookDataLoader.class);

		Map<UUID, Integer> basket = env.getSource();

		return dataLoader
			.loadMany(basket.keySet().stream().toList())
			.thenApply(books -> connectionService.getConnection(books, env))
			.thenApply(connection ->
				new DefaultConnection<>(
					connection
						.getEdges()
						.stream()
						.map(edge -> {
							Edge<Book> basketBookEdge = new BasketBookEdge(
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
