package com.umcs.enterprise;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity(name = "BookUser")
public class User {
    @Id
    @GeneratedValue
    private Long databaseId;
}
