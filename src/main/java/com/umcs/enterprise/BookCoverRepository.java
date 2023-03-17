package com.umcs.enterprise;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoverRepository extends JpaRepository<BookCover, Long> {
    public List<BookCover> findAllByBookId(Iterable<Long> ids);
}
