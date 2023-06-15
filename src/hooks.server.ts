import type { Handle } from '@sveltejs/kit';

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
