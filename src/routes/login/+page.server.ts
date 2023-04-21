import { graphql } from '$houdini';
import { redirect, type ServerLoad } from '@sveltejs/kit';
import type { Actions } from './$houdini';

export const load: ServerLoad = ({ locals }) => {
	return {};
};

const login = graphql(`
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
`);

export const actions: Actions = {
	default: async (event) => {
		const { username, password } = Object.fromEntries(await event.request.formData());

		if (typeof username !== 'string') {
			throw new Error('No username');
		}

		if (typeof password !== 'string') {
			throw new Error('No password');
		}

		const response = await login.mutate(
			{
				input: {
					username,
					password
				}
			},
			{ event }
		);

		if (response.data?.login?.__typename === 'LoginSuccess') {
			event.cookies.set('enterprise-token', response.data?.login.token || '');
			throw redirect(303, '/');
		}

		if (response.data?.login?.__typename === 'LoginError') {
			return {
				username: {
					value: username,
					error: response.data?.login?.username
				}
			};
		}

		return response;
	}
};
