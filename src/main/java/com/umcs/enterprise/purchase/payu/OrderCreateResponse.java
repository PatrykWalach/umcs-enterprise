package com.umcs.enterprise.purchase.payu;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderCreateResponse {

	private String redirectUri;
	private String orderId;
	private String extOrderId;
}
