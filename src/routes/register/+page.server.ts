import { graphql } from '$houdini';
import { fail, redirect } from '@sveltejs/kit';
import type { Actions } from './$houdini';

const register = graphql(`
	mutation Register($input: RegisterInput!) {
		register(input: $input) @required {
			__typename
			... on RegisterError {
				name @required
			}
			... on RegisterSuccess {
				__typename
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
				input: {
					user: { name: form.data.username, password: form.data.password }
				}
			},
			{ event }
		);

		if (response.data?.register?.__typename === 'RegisterSuccess') {
			throw redirect(303, '/');
		}

		if (response.data?.register.__typename === 'RegisterError') {
			return setError(form, 'username', response.data.register.name);
		}

		return fail(400, { form: form });
	}
};
