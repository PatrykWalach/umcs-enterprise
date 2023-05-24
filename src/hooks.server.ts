import type { Handle } from '@sveltejs/kit';
import { Client } from 'ketting';

export const handle: Handle = ({ event, resolve }) => {
	const token = event.cookies.get('enterprise-token');

	event.locals.client = new Client('http://localhost:8080/api');
	event.locals.client.use((request, next) => {
		// return next(request)
		console.log(request.url);
		return event.fetch(request.url, {
			method: request.method,
			body: request.body,
			headers: new Headers({ Accept: 'application/hal+json' })
		});
	});
	return resolve(event);
};
