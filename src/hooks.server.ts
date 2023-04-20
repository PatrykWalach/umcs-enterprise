import type { Handle } from '@sveltejs/kit';

import { GraphQLClient } from 'graphql-request';
import qs from 'query-string';

export const handle: Handle = ({ event, resolve }) => {
	const token = event.cookies.get('enterprise-token');

	const client = new GraphQLClient('http://localhost:8080/graphql', {
		fetch: event.fetch,
		errorPolicy: 'ignore',
		signal: event.request.signal as any,
		headers: new Headers({
			...(token && { Authorization: `Bearer ${token}` })
		})
	});
	event.locals.client = client;
	event.locals.formData = async <T>() => {
		return qs.parse(await event.request.text(), { parseBooleans: true, parseNumbers: true }) as T;
	};

	return resolve(event);
};
