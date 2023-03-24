package com.umcs.enterprise;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class Cover implements Node {

	@Id
	@GeneratedValue
	private Long databaseId;

	private int width;

	@OneToOne(fetch = FetchType.LAZY)
	private Book book;

	private String filename;

	private int height;
}
