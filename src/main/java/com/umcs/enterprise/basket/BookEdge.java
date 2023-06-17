package com.umcs.enterprise.basket;

import com.umcs.enterprise.book.Book;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class BookEdge implements SummableEdge {

	private Book book;
	private Integer quantity;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BookEdge bookEdge = (BookEdge) o;
		return book.equals(bookEdge.book);
	}

	@Override
	public int hashCode() {
		return Objects.hash(book);
	}
}
