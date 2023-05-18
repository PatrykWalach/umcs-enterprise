import { graphql } from '$houdini';
import { setToken } from '$lib/setToken';
import { fail, redirect, type ActionFailure } from '@sveltejs/kit';
import type { Actions } from './$houdini';

const login = graphql(`
	mutation Login($input: LoginInput!) {
		login(input: $input) @required {
			__typename
			... on LoginSuccess {
				token {
					...SetToken_token @mask_disable
				}
			}
			... on LoginError {
				username @required
			}
		}
	}
`);

import type { Validation } from 'sveltekit-superforms/index';
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
	default: async (event): Promise<ActionFailure<{ form: Validation<typeof schema> }>> => {
		const form = await superValidate(event.request, schema);

		if (!form.valid) {
			return fail(400, { form: form });
		}

		const response = await login.mutate(
			{
				input: form.data
			},
			{ event }
		);

		if (response.data?.login?.__typename === 'LoginSuccess') {
			setToken(event, response.data.login.token);
			throw redirect(303, '/');
		}

		if (response.data?.login.__typename === 'LoginError') {
			return setError(form, null, response.data.login.username);
		}

		return fail(400, { form: form });
	}
};
