import type { Handle } from '@sveltejs/kit';

import { GraphQLClient } from 'graphql-request';

export const handle: Handle = ({ event, resolve }) => {
	const token = event.cookies.get('enterprise-token');

	const client = new GraphQLClient('http://localhost:8080/graphql', {
		fetch: event.fetch,
		errorPolicy: 'ignore',
		signal: event.request.signal,
		headers: new Headers({
			...(token && { Authorization: `Bearer ${token}` })
		})
	});
	event.locals.client = client;
	return resolve(event);
};
