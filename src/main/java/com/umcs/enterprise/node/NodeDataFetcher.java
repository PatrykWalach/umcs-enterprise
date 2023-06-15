package com.umcs.enterprise.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.netflix.graphql.dgs.*;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.cover.CoverRepository;
import com.umcs.enterprise.node.GlobalId;
import com.umcs.enterprise.node.Node;
import com.umcs.enterprise.purchase.PurchaseRepository;
import com.umcs.enterprise.types.DeleteInput;
import com.umcs.enterprise.types.DeleteResult;
import com.umcs.enterprise.user.UserRepository;
import graphql.schema.DataFetchingEnvironment;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.springframework.security.access.annotation.Secured;

@DgsComponent
@RequiredArgsConstructor
public class NodeDataFetcher {

	@DgsData(parentType = "Node")
	public String id(DataFetchingEnvironment env) throws JsonProcessingException {
		return env.<Node>getSource().getId();
	}

	@DgsQuery
	public CompletableFuture<Node<UUID>> node(
		@InputArgument String id,
		DgsDataFetchingEnvironment enf
	) throws JsonProcessingException {
		GlobalId<String> globalId = GlobalId.from(id);

		DataLoader<UUID, Node<UUID>> loader = enf.getDataLoader(globalId.className() + "DataLoader");

		if (loader == null) {
			throw new DgsEntityNotFoundException();
		}

		return loader
			.load(UUID.fromString(globalId.databaseId()))
			.thenApply(Optional::ofNullable)
			.thenApply(optional -> optional.orElseThrow(DgsEntityNotFoundException::new));
	}

	private final BookRepository bookRepository;
	private final UserRepository userRepository;
	private final PurchaseRepository purchaseRepository;

	@DgsMutation
	@Secured("ADMIN")
	public DeleteResult delete(@InputArgument DeleteInput input) {
		Map<String, Consumer<UUID>> delete = Map.of(
			"Book",
			bookRepository::deleteById,
			"User",
			userRepository::deleteById,
			"Purchase",
			purchaseRepository::deleteById
		);

		GlobalId globalId = GlobalId.from(input.getId());

		Consumer<UUID> consumer = delete.get(globalId.className());

		if (consumer == null) {
			throw new DgsEntityNotFoundException();
		}

		consumer.accept(globalId.databaseId());

		return DeleteResult.newBuilder().id(input.getId()).build();
	}
}
