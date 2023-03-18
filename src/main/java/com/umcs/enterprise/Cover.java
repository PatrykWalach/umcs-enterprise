package com.umcs.enterprise;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder(builderMethodName ="newBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class Cover {

	@Id
	@GeneratedValue
	private Long databaseId;

	
	private int width;

	
	@OneToOne(mappedBy="cover")
	private Book book;

	
	private String filename;

	
	private int height;
}
