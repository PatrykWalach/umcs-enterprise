package com.umcs.enterprise.purchase.payu;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class OrderCreateRequest {
     private  String customerIp= "123.123.123.123";
     private  String extOrderId;
     private  String merchantPosId;
     private  String description= "Books purchase";
     private  String additionalDescription;
     private  String visibleDescription;
     private  String statementDescription;
     private  String currencyCode= "PLN";
     private  String totalAmount;
     private Buyer buyer;
     private  String continueUrl;
     private  String notifyUrl;
     private List<Product> products;


}
