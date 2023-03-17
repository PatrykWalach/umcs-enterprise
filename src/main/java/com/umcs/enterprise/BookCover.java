package com.umcs.enterprise;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class BookCover {

	@Id
	@GeneratedValue
	private Long databaseId;

	@Column
	private int width;

	@Column(insertable=false, updatable=false)
	private Long bookId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bookId")
	private Book book;

	@Column
	private String filename;

	@Column
	private int height;

	public BookCover(Long databaseId, int width, Book book, String filename, int height) {
		this.databaseId = databaseId;
		this.width = width;
		this.book = book;
		this.filename = filename;
		this.height = height;
	}
}
