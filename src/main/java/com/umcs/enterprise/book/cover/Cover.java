package com.umcs.enterprise.book.cover;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Embeddable;
import lombok.*;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cover {

	private Integer width;

	@JsonIgnore
	private Integer height;

	private String url;
}
