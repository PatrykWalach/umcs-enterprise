import { redirect, type Actions } from '@sveltejs/kit';

export const actions: Actions = {
	default({ cookies }) {
		cookies.delete('enterprise-token');
		throw redirect(303, '/');
	}
};
