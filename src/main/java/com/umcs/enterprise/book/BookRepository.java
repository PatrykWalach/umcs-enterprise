package com.umcs.enterprise.book;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {
	@Query(
		value = "SELECT b FROM BookPurchase t1, BookPurchase t2 JOIN t2.book b WHERE t1.purchase = t2.purchase AND t1.book <> t2.book AND t1.book.databaseId = ?1 GROUP BY t1.book, t2.book, b ORDER BY COUNT(t1.purchase) DESC, b.popularity DESC, b.createdAt"
	)
	List<Book> findByOrdersBooksDatabaseId(Long databaseId);
}
