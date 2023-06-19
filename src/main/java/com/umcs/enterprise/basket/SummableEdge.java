package com.umcs.enterprise.basket;

import com.umcs.enterprise.book.Book;

public interface SummableEdge {
	Book getBook();
	Integer getQuantity();
	default Long getPrice(){
		return getQuantity() * getBook().getPrice();
	}
}
