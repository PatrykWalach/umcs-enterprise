package com.umcs.enterprise;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {
	@Query(
		value = "SELECT b FROM BookOrder t1, BookOrder t2 JOIN t2.book b WHERE t1.order = t2.order AND t1.book < t2.book AND t1.book.databaseId = ?1 GROUP BY t1.book, t2.book, b ORDER BY COUNT(t1.order) DESC, b.popularity DESC"
	)
	List<Book> findByOrdersBooksDatabaseId(Long databaseId);
}
