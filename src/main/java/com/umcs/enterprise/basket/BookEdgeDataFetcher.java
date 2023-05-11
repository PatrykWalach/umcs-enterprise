package com.umcs.enterprise.basket;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.umcs.enterprise.ConnectionService;
import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.book.BookDataLoader;
import graphql.relay.Connection;
import graphql.relay.Edge;
import graphql.schema.DataFetchingEnvironment;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;

@DgsComponent
@RequiredArgsConstructor
public class BookEdgeDataFetcher {

	@NonNull
	private final ConnectionService connectionService;

	@DgsData(parentType = "BasketBookEdge")
	public CompletableFuture<BigDecimal> price(DgsDataFetchingEnvironment env) {
		var edge = env.<Edge<BookEdge>>getSource();

		DataLoader<UUID, Book> dataLoader = env.getDataLoader(BookDataLoader.class);
		return dataLoader
			.load(edge.getNode().getBook().getDatabaseId())
			.thenApply(book -> book.getPrice().multiply(BigDecimal.valueOf(edge.getNode().getQuantity()))
			);
	}

	@DgsData(parentType = "BasketBookEdge")
	public Integer quantity(DgsDataFetchingEnvironment env) {
		var edge = env.<Edge<BookEdge>>getSource();

		return edge.getNode().getQuantity();
	}

	@DgsData(parentType = "BasketBookEdge")
	public CompletableFuture<Book> node(DgsDataFetchingEnvironment env) {
		DataLoader<UUID, Book> dataLoader = env.getDataLoader(BookDataLoader.class);

		var edge = env.<Edge<BookEdge>>getSource();

		return dataLoader.load(edge.getNode().getBook().getDatabaseId());
	}

	@DgsData(parentType = "Basket")
	public Connection<BookEdge> books(DgsDataFetchingEnvironment env) {
		Basket basket = env.getSource();

		return connectionService.getConnection(basket.getBooks(), env);
	}
}
