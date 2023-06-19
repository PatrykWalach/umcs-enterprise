package com.umcs.enterprise.purchase.payu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umcs.enterprise.basket.SummableService;
import com.umcs.enterprise.purchase.Purchase;
import com.umcs.enterprise.types.PurchaseStatus;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class PayuService {

	private final WebClient api = WebClient
		.builder()
		.baseUrl("https://secure.snd.payu.com/api/v2_1")
		.build();
	private final WebClient auth = WebClient
		.builder()
		.baseUrl("https://secure.snd.payu.com/pl/standard/user/oauth")
		.build();

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
		return auth
			.post()
			.uri("/authorize")
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
	}

	@NonNull
	private final ObjectMapper mapper;

	@Value("${client.address}")
	public String CLIENT_ADDRESS;

	public OrderCreateResponse save(Purchase purchase) {
		OrderCreateRequest body = Mappers
			.getMapper(OrderCreateRequestMapper.class)
			.purchaseToRequest(
				purchase,
				posId,
				"http://" + CLIENT_ADDRESS + ":5173/purchase/" + purchase.getId(),
				summableService
			);

		OAuthResponse response = authorize();

		assert "bearer".equals(response.getGrantType());

		return api
			.post()
			.uri("/orders")
			//					.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.header("Authorization", "Bearer " + response.getAccessToken())
			.bodyValue(body)
			.retrieve()
			.bodyToMono(OrderCreateResponse.class)
			.block();
	}

	public boolean isPaid(Purchase purchase) {
		if (purchase.getStatus().equals(PurchaseStatus.MADE)) {
			//            request
			//            if paid update model
			//            purchaseService
		}

		return purchase.getStatus().equals(PurchaseStatus.PAID);
	}
}
