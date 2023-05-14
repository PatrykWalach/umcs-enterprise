import { setSession } from '$houdini';
import { redirect, type Actions } from '@sveltejs/kit';

export const actions: Actions = {
	async default(event) {
		const token = event.cookies.get('enterprise-token');

		console.log({ token });

		setSession(event, {
			token: undefined
		});

		if (!token) {
			throw redirect(303, '/');
		}

		const response = await event.fetch(`http://localhost:8080/oauth2/revoke`, {
			body: new URLSearchParams({
				token
			}),
			method: 'POST',
			headers: new Headers({
				'Content-type': 'application/x-www-form-urlencoded',
				Authorization: `Bearer ${token}`
			})
		});

		console.log(await response.json());

		event.cookies.delete('enterprise-token');

		throw redirect(303, '/');
	}
};
