package com.umcs.enterprise;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoverRepository extends JpaRepository<BookCover, Long> {
	public List<BookCover> findAllByBookId(Iterable<Long> ids);
}
