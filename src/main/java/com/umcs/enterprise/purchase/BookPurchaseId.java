package com.umcs.enterprise.purchase;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import java.util.UUID;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder(builderMethodName = "newBuilder")
@AllArgsConstructor
@NoArgsConstructor
public class BookPurchaseId {

	@Column(name = "purchase_id")
	Long purchaseId;

	@Column(name = "book_id")
	Long bookId;
}
