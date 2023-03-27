import { graphql } from '$lib/gql';
import { redirect, type ServerLoad } from '@sveltejs/kit';
import type { Actions } from './$types';

export const load: ServerLoad = ({ locals }) => {
	return {};
};

export const actions: Actions = {
	default: async ({ locals, request, cookies }) => {
		const { username, password } = Object.fromEntries(await request.formData());

		if (typeof username !== 'string') {
			throw new Error('No username');
		}

		if (typeof password !== 'string') {
			throw new Error('No password');
		}

		const data = await locals.client.request(
			graphql(`
				mutation Register($input: RegisterInput!) {
					register(input: $input) {
						token
					}
				}
			`),
			{
				input: {
					username,
					password
				}
			}
		);

		if (data.register?.token) {
			cookies.set('enterprise-token', data.register?.token);
			throw redirect(303, '/');
		}

		return {};
	}
};
