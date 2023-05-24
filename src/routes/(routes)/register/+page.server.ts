
import { setToken } from '$lib/setToken';
import { fail, redirect } from '@sveltejs/kit';
import type { Actions } from './$types';

function graphql(){}

const register = graphql(`
	mutation Register($input: RegisterInput!) {
		register(input: $input) @required {
			__typename
			... on RegisterSuccess {
				token {
					...SetToken_token @mask_disable
				}
			}
			... on RegisterError {
				username @required
			}
		}
	}
`);

import { setError, superValidate } from 'sveltekit-superforms/server';
import { z } from 'zod';

const schema = z.object({
	username: z.string(),
	password: z.string()
});

export const load = async () => {
	return {
		form: await superValidate(schema)
	};
};

export const actions: Actions = {
	default: async (event) => {
		const form = await superValidate(event.request, schema);

		if (!form.valid) {
			return fail(400, { form: form });
		}

		const response = await register.mutate(
			{
				input: form.data
			},
			{ event }
		);

		if (response.data?.register?.__typename === 'RegisterSuccess') {
			setToken(event, response.data.register.token);
			throw redirect(303, '/');
		}

		if (response.data?.register.__typename === 'RegisterError') {
			return setError(form, 'username', response.data.register.username);
		}

		return fail(400, { form: form });
	}
};
