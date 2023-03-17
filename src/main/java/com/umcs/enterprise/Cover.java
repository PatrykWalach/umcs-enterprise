package com.umcs.enterprise;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cover {

	@Id
	@GeneratedValue
	private Long databaseId;

	@Column
	private int width;

	@Column
	private String filename;

	@Column
	private int height;
}
