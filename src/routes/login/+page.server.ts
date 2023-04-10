import { graphql } from '$gql';
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
				mutation Login($input: LoginInput!) {
					login(input: $input) {
						__typename
						... on LoginSuccess {
							token
						}
						... on LoginError {
							username
						}
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

		if (data.login?.__typename === 'LoginSuccess') {
			cookies.set('enterprise-token', data.login.token || '');
			throw redirect(303, '/');
		}

		if (data.login?.__typename === 'LoginError') {
			return {
				username: {
					value: username,
					error: data.login?.username
				}
			};
		}
	}
};
