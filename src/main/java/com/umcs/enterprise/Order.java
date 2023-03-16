package com.umcs.enterprise;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Order implements Node {
    @Id
    @GeneratedValue
    private Long databaseId;

    @Override
    public Long getDatabaseId() {
        return null;
    }
}
