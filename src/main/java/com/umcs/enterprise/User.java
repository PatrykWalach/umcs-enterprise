package com.umcs.enterprise;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Entity(name = "BookUser")
@Getter
@Setter
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue
	private Long databaseId;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<Order> orders;
}
