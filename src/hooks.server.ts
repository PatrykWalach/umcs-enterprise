import type { Handle } from '@sveltejs/kit';

import { GraphQLClient } from 'graphql-request';

export const handle: Handle = ({ event, resolve }) => {
	const client = new GraphQLClient('http://localhost:8080/graphql', {
		fetch: event.fetch,
		errorPolicy: 'ignore'
	});
	event.locals.client = client;
	return resolve(event);
};
