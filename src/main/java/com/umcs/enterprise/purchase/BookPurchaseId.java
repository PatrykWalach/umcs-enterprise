package com.umcs.enterprise.purchase;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import lombok.*;

import java.util.UUID;

@Embeddable
@Getter
@Setter
@Builder(builderMethodName = "newBuilder")
@AllArgsConstructor
@NoArgsConstructor
public class BookPurchaseId {
    @Column(name = "purchase_id")
    UUID purchaseId;

    @Column(name = "book_id")
    UUID bookId;
}
