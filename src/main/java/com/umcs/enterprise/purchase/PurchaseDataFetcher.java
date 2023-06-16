package com.umcs.enterprise.purchase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.netflix.graphql.dgs.*;
import com.umcs.enterprise.ConnectionService;
import com.umcs.enterprise.basket.Basket;
import com.umcs.enterprise.basket.BasketService;
import com.umcs.enterprise.node.GlobalId;
import com.umcs.enterprise.types.*;
import com.umcs.enterprise.user.User;
import com.umcs.enterprise.user.UserDataLoader;
import graphql.relay.Connection;
import graphql.schema.DataFetchingEnvironment;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;

@DgsComponent
@RequiredArgsConstructor
public class PurchaseDataFetcher {

	private ConnectionService connectionService;

	@DgsData(parentType = "Purchase")
	public CompletableFuture<User> user(DgsDataFetchingEnvironment dfe) {
		return dfe
			.<UUID, User>getDataLoader(UserDataLoader.class)
			.load(dfe.<Purchase>getSource().getUser().getDatabaseId());
	}

	@NonNull
	private final PurchaseService purchaseService;

	@NonNull
	private final BasketService basketService;

	private final BookPurchaseRepository bookPurchaseRepository;

	@DgsData(parentType = "Viewer")
	public Connection<Purchase> purchases(
		@InputArgument PurchaseWhere where,
		DataFetchingEnvironment dfe
	) {
		return connectionService.getConnection(purchaseService.findAllWhere(), dfe);
	}

	@DgsMutation
	@Secured("USER")
	public MakePurchaseResult makePurchase(@InputArgument MakePurchaseInput input)
		throws JsonProcessingException {
		Basket basket = basketService.getBasket(input.getBasket().getId());

		var purchase = purchaseService.save(Purchase.newBuilder().build());

		purchase.setBooks(
			bookPurchaseRepository.saveAll(
				(
					basket
						.getBooks()
						.stream()
						.map(book ->
							Mappers.getMapper(BookEdgeMapper.class).bookEdgeToBookPurchase(book, purchase)
						)
						.toList()
				)
			)
		);

		return MakePurchaseResult
			.newBuilder()
			.purchase(purchase)
			.basket(basketService.getBasket(null))
			.build();
	}

	@DgsMutation
	public CompletableFuture<Purchase> sendPurchase(
		@InputArgument SendPurchaseInput input,
		DgsDataFetchingEnvironment dfe
	) throws JsonProcessingException {
		GlobalId<String> from = GlobalId.from(input.getID());
		assert Objects.equals(from.className(), "Purchase");

		return dfe
			.<UUID, Purchase>getDataLoader(PurchaseDataLoader.class)
			.load(UUID.fromString(from.databaseId()))
			.thenApply(purchase -> {
				assert Objects.equals(purchase.getStatus(), PurchaseStatus.PAID);
				purchase.setStatus(PurchaseStatus.SENT);

				return purchaseService.save(purchase);
			});
	}
}
