package com.umcs.enterprise;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Book implements Node {
    @Id
    @GeneratedValue
    private Long databaseId;
    @Column
    private String author;
    @Column
    private String title;
    @OneToOne(fetch = FetchType.LAZY)
    private BookCover cover;

    @Column
    private int price;
    @Column
    private ZonedDateTime createdAt;
    @Column
    private int popularity;


}
