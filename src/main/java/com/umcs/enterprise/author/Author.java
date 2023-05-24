package com.umcs.enterprise.author;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.umcs.enterprise.book.Book;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import lombok.*;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.rest.core.annotation.RestResource;

@Getter
@Setter
@ToString
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @Type(type = "uuid-char")
    private UUID id;

	@JsonIgnore
	@LastModifiedDate
	private LocalDateTime lastModifiedDate;

    private
    @LastModifiedDate @JsonIgnore LocalDateTime lastModifiedDate;
    private @Version Long version;

	private String name;

    @ManyToMany(mappedBy = "authors")
    // @RestResource(exported = false)
   @Builder.Default
    @ToString.Exclude
    private Set<Book> books = new LinkedHashSet<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Author author = (Author) o;
		return getId() != null && Objects.equals(getId(), author.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
