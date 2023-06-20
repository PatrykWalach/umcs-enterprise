package com.umcs.enterprise.purchase.payu;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class OrderRetrieveRequest {
    private List<Order> orders;
}
