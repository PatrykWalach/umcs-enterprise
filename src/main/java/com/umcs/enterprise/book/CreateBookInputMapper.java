package com.umcs.enterprise.book;

import com.umcs.enterprise.types.CreateBookInput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CreateBookInputMapper {
	@Mapping(target = "synopsis", ignore = true)
	@Mapping(target = "purchases", ignore = true)
	@Mapping(target = "popularity", constant = "0L")
	@Mapping(target = "databaseId", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "price", source = "price.raw")
	@Mapping(target = "cover", ignore = true)
	com.umcs.enterprise.book.Book createBookInputToBook(CreateBookInput input);


}
