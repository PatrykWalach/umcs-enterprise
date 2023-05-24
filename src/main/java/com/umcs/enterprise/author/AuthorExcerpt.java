package com.umcs.enterprise.author;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(types = { Author.class }, name = "excerpt")
public interface AuthorExcerpt {
	String getName();
	UUID getId();
}
