package com.umcs.enterprise.book.cover;

import com.umcs.enterprise.book.cover.Cover;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Covers {

	private String transformation;

	@ElementCollection
	private List<Cover> covers;
}
