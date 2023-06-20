package com.umcs.enterprise.purchase.payu;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OrderRetrieveRequest {

	private List<Order> orders;
}
