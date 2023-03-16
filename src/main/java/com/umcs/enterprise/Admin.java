package com.umcs.enterprise;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
//org.springframework.security.core.userdetails.User

@Entity
public class Admin implements Node {
    @Id
    @GeneratedValue
    private Long databaseId;

    @Override
    public Long getDatabaseId() {
        return null;
    }
}
