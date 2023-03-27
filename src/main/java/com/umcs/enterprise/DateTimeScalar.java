package com.umcs.enterprise;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsRuntimeWiring;
import com.umcs.enterprise.user.User;
import graphql.scalars.ExtendedScalars;
import graphql.schema.idl.RuntimeWiring;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

@DgsComponent
public class DateTimeScalar {

	@DgsRuntimeWiring
	public RuntimeWiring.Builder addScalar(RuntimeWiring.Builder builder) {
		return builder.scalar(ExtendedScalars.DateTime);
	}
}
