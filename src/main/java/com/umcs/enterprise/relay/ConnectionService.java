package com.umcs.enterprise.relay;

import static java.lang.String.format;

import graphql.relay.SimpleListConnection;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ConnectionService {

	private void validateConnectionArgs(DataFetchingEnvironment env) {
		Integer first = env.getArgument("first");
		Integer last = env.getArgument("last");
		String after = env.getArgument("after");
		String before = env.getArgument("before");

		if (first == null && last == null) {
			throw new IllegalArgumentException(("Exactly one of 'first' or 'last' is required"));
		}

		if (first != null && last != null) {
			throw new IllegalArgumentException(("Exactly one of 'first' or 'last' is required"));
		}
		if (first != null && before != null) {
			throw new IllegalArgumentException(
				format(
					"The %s.%s connection field does not allow a 'before' argument with 'first'",
					env.getParentType(),
					env.getField().getName()
				)
			);
		}
		if (last != null && after != null) {
			throw new IllegalArgumentException(
				format(
					"The %s.%s connection field does not allow a 'last' argument with 'after'",
					env.getParentType(),
					env.getField().getName()
				)
			);
		}

		if (first != null && first < 1) {
			throw new IllegalArgumentException(
				format("The page size must more than zero: 'first'=%s", first)
			);
		}

		if (last != null && last < 1) {
			throw new IllegalArgumentException(
				format("The page size must more than zero: 'last'=%s", last)
			);
		}
	}

	public <T> graphql.relay.Connection<T> getConnection(List<T> data, DataFetchingEnvironment env) {
		validateConnectionArgs(env);
		return new SimpleListConnection<>(data).get(env);
	}
}
