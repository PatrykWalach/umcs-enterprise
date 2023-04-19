package com.umcs.enterprise.purchase;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.umcs.enterprise.user.User;
import com.umcs.enterprise.user.UserDataLoader;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@DgsComponent
public class PurchaseDataFetcher {

	@DgsData(parentType = "Purchase")
	public CompletableFuture<User> user(DgsDataFetchingEnvironment dfe) {
		return dfe
			.<UUID, User>getDataLoader(UserDataLoader.class)
			.load(dfe.<Purchase>getSource().getUser().getDatabaseId());
	}
}
