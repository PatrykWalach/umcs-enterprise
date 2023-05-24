package com.umcs.enterprise.order;

import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.io.Serializable;
import java.util.UUID;


@Getter

@ToString
@Builder

@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class BookEdgeId implements Serializable {

    @Type(type="uuid-char")
    private UUID bookId;
    @Type(type="uuid-char")
    private UUID orderId;
}
