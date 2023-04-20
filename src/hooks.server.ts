import type { Handle } from '@sveltejs/kit';

import { setSession } from '$houdini';

export const handle: Handle = ({ event, resolve }) => {
	const token = event.cookies.get('enterprise-token');

	setSession(event, {
		token
	});

	return resolve(event);
};
