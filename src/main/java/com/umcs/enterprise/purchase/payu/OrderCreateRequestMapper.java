package com.umcs.enterprise.purchase.payu;

import com.umcs.enterprise.basket.SummableService;
import com.umcs.enterprise.purchase.BookPurchase;
import com.umcs.enterprise.purchase.Purchase;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper
public interface OrderCreateRequestMapper {

    @Mapping(target = "products", source = "purchase.books")
    @Mapping(target = "visibleDescription", ignore = true)
    @Mapping(target = "statementDescription", ignore = true)
    @Mapping(target = "notifyUrl", ignore = true)
    @Mapping(target = "continueUrl", source = "continueUrl")
    @Mapping(target = "buyer", ignore = true)
    @Mapping(target = "additionalDescription", ignore = true)
    @Mapping(target = "totalAmount", source = "purchase.books")
    @Mapping(target = "merchantPosId", source = "posId")
    @Mapping(target = "extOrderId", source = "purchase.id")
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "customerIp", ignore = true)
    @Mapping(target = "currencyCode", ignore = true)
    OrderCreateRequest purchaseToRequest(Purchase purchase, String posId, String continueUrl, @Context SummableService summableService);

    @Mapping(target = "unitPrice", source = "price")
    @Mapping(target = "name", source = "book.title")
    @Mapping(target = "virtual", ignore = true)
    @Mapping(target = "listingDate", ignore = true)
    Product bookToPurchase(BookPurchase book);

  default   String totalAmount(Set<BookPurchase> books, @Context SummableService summableService){
        return summableService.sumPrice(books).toString();
    }


}
