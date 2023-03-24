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
import cookielib from 'cookie';

const cookies = ['basket'];

export const handleFetch = (async ({ event, request, fetch }) => {
	cookies
		.map((cookie) => event.cookies.get(cookie))
		.flatMap((cookie) => (cookie ? [cookie] : []))
		.forEach((cookie) => request.headers.set('cookie', cookielib.serialize('basket', cookie)));

	const response = await fetch(request);
	const setCookie = response.headers.get('set-cookie');

	if (!setCookie) {
		return response;
	}
	const { Path, ...rest } = cookielib.parse(setCookie);

	cookies
		.flatMap((cookie): [string, string][] => (rest[cookie] ? [[cookie, rest[cookie]]] : []))
		.forEach(([key, cookie]) => {
			event.cookies.set(key, cookie, {
				path: Path
			});
		});

	return response;
}) satisfies HandleFetch;
