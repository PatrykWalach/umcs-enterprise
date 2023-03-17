package com.umcs.enterprise;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "BookOrder")
@Getter
@Setter
@NoArgsConstructor
public class Order implements Node {

	@Id
	@GeneratedValue
	private Long databaseId;
}
