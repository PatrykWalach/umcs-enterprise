import type { Handle } from '@sveltejs/kit';

import { GraphQLClient } from 'graphql-request';

export const handle: Handle = ({ event, resolve }) => {
	const client = new GraphQLClient('http://localhost:8080/graphql', {
		fetch: event.fetch,
		errorPolicy: 'ignore',
		signal: event.request.signal
	});
	event.locals.client = client;
	return resolve(event);
};

import type { HandleFetch } from '@sveltejs/kit';
import cookie from 'cookie';

export const handleFetch = (async ({ event, request, fetch }) => {
	request.headers.set('cookie', cookie.serialize('basket', event.cookies.get('basket')));

	const response = await fetch(request);
	const setCookie = response.headers.get('set-cookie');

	if (!setCookie) {
		return response;
	}
	const { Path, basket } = cookie.parse(setCookie);

	if (!basket) {
		return response;
	}

	event.cookies.set('basket', basket, {
		path: Path
	});

	return response;
}) satisfies HandleFetch;
