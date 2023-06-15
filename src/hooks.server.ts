import type { Handle, HandleFetch } from '@sveltejs/kit';

import { setSession } from '$houdini';
import { BASKET_COOKIE, TOKEN_COOKIE } from '$lib/constants';

export const handle: Handle = ({ event, resolve }) => {
	const token = event.cookies.get(TOKEN_COOKIE);

	setSession(event, {
		token,
		basket: event.cookies.get(BASKET_COOKIE)
	});

	return resolve(event);
};

// import scp from 'cookie';

// export const handleFetch: HandleFetch = async ({ request, fetch, event }) => {
// 	let sessionId = event.cookies.get('JSESSIONID');

// 	if (request.url.startsWith('http://localhost:8080/graphql') && sessionId) {
// 		request.headers.set('JSESSIONID', sessionId);
// 	}

// 	const response = await fetch(request);

// 	const cookies = response.headers.get('set-cookie');

// 	if (request.url.startsWith('http://localhost:8080/graphql') && cookies) {
// 		console.log(cookies)
// 		sessionId = scp.parse(cookies)['JSESSIONID'];

// 		sessionId &&
// 			event.cookies.set('JSESSIONID', sessionId, {
// 				path: '/'
// 			});
// 	}

// 	return response;
// };
