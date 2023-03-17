package com.umcs.enterprise;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface BookCoverRepository extends JpaRepository<BookCover, Long> {
    public List<BookCover> findByBookIdIn(Collection<Long> ids);
}
