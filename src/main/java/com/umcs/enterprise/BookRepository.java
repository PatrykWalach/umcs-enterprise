package com.umcs.enterprise;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {
	//      List<Book> findByOrdersBooksDatabaseId(Long databaseId) ;

	@Query(
		nativeQuery = true,
		value = "SELECT book.* FROM books_orders as T1 JOIN books_orders as T2 ON T1.order_id = T2.order_id AND T1.book_id < T2.book_id JOIN book ON T2.book_id = book.database_id WHERE T1.book_id = ?1 GROUP BY T1.book_id, T2.book_id, book.database_id ORDER BY COUNT(T1.order_id) DESC"
	)
	List<Book> findByOrdersBooksDatabaseId(Long databaseId);
}
