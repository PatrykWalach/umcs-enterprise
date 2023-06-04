package com.umcs.enterprise.cover;

import com.umcs.enterprise.book.Book;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class Cover {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long databaseId;

	private String uuid;

	private String url;
	@OneToOne(fetch = FetchType.LAZY)
	private Book book;
}
