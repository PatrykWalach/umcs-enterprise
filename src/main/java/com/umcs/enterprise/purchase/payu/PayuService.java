package com.umcs.enterprise.purchase.payu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umcs.enterprise.basket.SummableService;
import com.umcs.enterprise.purchase.Purchase;
import com.umcs.enterprise.purchase.PurchaseRepository;
import com.umcs.enterprise.types.PurchaseStatus;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class PayuService {

	private final WebClient api = WebClient.builder().baseUrl("https://secure.snd.payu.com").build();

	@Value("${payu.pos-id}")
	private String posId;

	@Value("${payu.client-id}")
	private String clientId;

	@Value("${payu.client-secret}")
	private String clientSecret;

	@Value("${payu.second-key}")
	private String secondKey;

	@NonNull
	private final SummableService summableService;

	private OAuthResponse authorize() {
		var response = api
			.post()
			.uri("/pl/standard/user/oauth/authorize")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.body(
				BodyInserters
					.fromFormData("grant_type", "client_credentials")
					.with("client_id", clientId)
					.with("client_secret", clientSecret)
			)
			.retrieve()
			.bodyToMono(OAuthResponse.class)
			.block();

		assert "bearer".equals(response != null ? response.getGrantType() : null);
		return response;
	}

	@NonNull
	private final ObjectMapper mapper;

	@Value("${client.address}")
	public String CLIENT_ADDRESS;

	private final PurchaseRepository purchaseRepository;

	public OrderCreateResponse save(Purchase purchase) {
		OrderCreateRequest body = Mappers
			.getMapper(OrderCreateRequestMapper.class)
			.purchaseToRequest(
				purchase,
				posId,
	
				 CLIENT_ADDRESS + ":5173/purchase/" + purchase.getId(),
				summableService
			);

		OAuthResponse response = authorize();

		return api
			.post()
			.uri("/api/v2_1/orders")
			//					.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.header("Authorization", "Bearer " + response.getAccessToken())
			.bodyValue(body)
			.retrieve()
			.bodyToMono(OrderCreateResponse.class)
			.block();
	}

	public PurchaseStatus getStatus(Purchase purchase) {
		if (!(purchase.getStatus().equals(PurchaseStatus.MADE) && purchase.getOrderId() != null)) {
			return purchase.getStatus();
		}

		OrderRetrieveRequest response = api
			.get()
			.uri("/api/v2_1/orders/" + purchase.getOrderId())
			.header("Authorization", "Bearer " + authorize().getAccessToken())
			.retrieve()
			.bodyToMono(OrderRetrieveRequest.class)
			.block();

		if (
			response != null &&
			response
				.getOrders()
				.stream()
				.anyMatch(order ->
					order.orderId().equals(purchase.getOrderId()) && order.status().equals("COMPLETED")
				)
		) {
			purchase.setStatus(PurchaseStatus.PAID);
			purchaseRepository.save(purchase);
		}

		return purchase.getStatus();
	}
}
