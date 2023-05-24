package com.umcs.enterprise.order;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter

@ToString
@Builder
@Entity( name = "\"order\"")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @Type(type="uuid-char")
    private UUID id;

    private @JsonIgnore
    @LastModifiedDate LocalDateTime lastModifiedDate;
    private @org.springframework.data.annotation.Version Long version;



    @OneToMany(mappedBy = "order")
    private Set<BookEdge> books;


    @Setter private OrderStatus status;

}
