package com.umcs.enterprise.basket;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.umcs.enterprise.ConnectionService;
import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.book.BookDataLoader;
import graphql.relay.Edge;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;

@DgsComponent
@RequiredArgsConstructor
public class SummableEdgeDataFetcher {

	@NonNull
	private final ConnectionService connectionService;

	@DgsData(parentType = "SummableBookEdge")
	public Long price(DgsDataFetchingEnvironment env) {
		var edge = env.<Edge<SummableEdge>>getSource();

		return edge.getNode().getPrice();
	}

	@DgsData(parentType = "SummableBookEdge")
	public Integer quantity(DgsDataFetchingEnvironment env) {
		var edge = env.<Edge<SummableEdge>>getSource();

		return edge.getNode().getQuantity();
	}

	@DgsData(parentType = "SummableBookEdge")
	public CompletableFuture<Book> node(DgsDataFetchingEnvironment env) {
		DataLoader<Long, Book> dataLoader = env.getDataLoader(BookDataLoader.class);

		var edge = env.<Edge<SummableEdge>>getSource();

		return dataLoader.load(edge.getNode().getBook().getDatabaseId());
	}
}
