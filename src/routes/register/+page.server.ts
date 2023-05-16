import { graphql } from '$houdini';
import { redirect } from '@sveltejs/kit';
import type { Actions } from './$houdini';
import { setToken } from '$lib/setToken';

const register = graphql(`
	mutation Register($input: RegisterInput!) {
		register(input: $input) {
			__typename
			... on RegisterSuccess {
				token {
					...SetToken_token @mask_disable
				}
			}
			... on RegisterError {
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

		const response = await register.mutate(
			{
				input: {
					username,
					password
				}
			},
			{ event }
		);

		if (response.data?.register?.__typename === 'RegisterSuccess') {
 
			setToken(event, response.data?.register.token)
			throw redirect(303, '/');
		}

		if (response.data?.register?.__typename === 'RegisterError') {
			return {
				username: {
					value: username,
					error: response.data?.register?.username
				}
			};
		}

		return response;
	}
};
