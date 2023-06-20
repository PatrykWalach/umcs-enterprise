import { dev } from '$app/environment';
import { PUBLIC_SERVER_ADDRESS } from '$env/static/public';
import { setSession } from '$houdini';
import { BASKET_COOKIE, TOKEN_COOKIE } from '$lib/constants';
import { redirect, type Actions } from '@sveltejs/kit';

export const actions: Actions = {
	async default(event) {
		const token = event.cookies.get(TOKEN_COOKIE);

		setSession(event, {
			token: undefined,
			basket: event.cookies.get(BASKET_COOKIE)
		});

		if (!token) {
			throw redirect(303, '/');
		}

		await event.fetch(`${PUBLIC_SERVER_ADDRESS}/logout`, {
			method: 'POST',
			headers: new Headers({
				Authorization: `Bearer ${token}`
			})
		});

		event.cookies.delete(TOKEN_COOKIE, {
			path: '/',
			sameSite: true,
			secure: !dev,
			httpOnly: true
		});

		throw redirect(303, '/');
	}
};
