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

		const {data,error} = await locals.client.mutation(
			graphql(`
				mutation Register($input: RegisterInput!) {
					register(input: $input) {
						__typename
						... on RegisterSuccess {
							token
						}
						... on RegisterError {
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

		if (!data) {
			throw error;
		}

		if (data.register?.__typename === 'RegisterSuccess') {
			cookies.set('enterprise-token', data.register.token || '');
			throw redirect(303, '/');
		}

		if (data.register?.__typename === 'RegisterError') {
			return {
				username: {
					value: username,
					error: data.register?.username
				}
			};
		}
	}
};
