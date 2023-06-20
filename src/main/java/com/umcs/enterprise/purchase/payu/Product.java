package com.umcs.enterprise.purchase.payu;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Product {

	private String name;
	private String unitPrice;
	private String quantity;
	private String virtual;
	private String listingDate;
}
