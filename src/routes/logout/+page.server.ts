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

		await event.fetch(`http://localhost:8080/logout`, {
			method: 'POST',
			headers: new Headers({
				Authorization: `Bearer ${token}`
			})
		});

		event.cookies.delete('enterprise-token');

		throw redirect(303, '/');
	}
};
