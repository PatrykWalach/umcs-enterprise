package com.umcs.enterprise.book;

import com.umcs.enterprise.author.Author;
import com.umcs.enterprise.book.cover.Cover;
import com.umcs.enterprise.book.cover.Covers;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class BookProjectionBean {

	public Map<Integer, String> coversToMap(Covers covers) {
		return covers.getCovers().stream().collect(Collectors.toMap(Cover::getWidth, Cover::getUrl));
	}

	public List<String> getNames(Set<Author> authors) {
		return authors.stream().map(Author::getName).collect(Collectors.toList());
	}
}
