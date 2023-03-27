package com.umcs.enterprise.cover;

import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.node.Node;
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "database_id", nullable = false)
	private Long databaseId;

	private int width;

	@OneToOne(fetch = FetchType.LAZY)
	private Book book;

	private String filename;

	private int height;
}
