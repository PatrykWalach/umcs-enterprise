import { graphql } from '$houdini';
import { fail } from '@sveltejs/kit';
import { z } from 'zod';
import type { Actions } from './$houdini';

const schema = z.object({
	releasedAt: z.coerce.date(),
	title: z.string(),
	author: z.string(),
	price: z.coerce.number().safe().positive(),
	cover: z.any()
});

import { redirect, type ActionFailure } from '@sveltejs/kit';

import type { Validation } from 'sveltekit-superforms/index';
import { setError, superValidate } from 'sveltekit-superforms/server';

export const load = async () => {
	return {
		form: await superValidate(schema)
	};
};

const addBook = graphql(`
	mutation AddBook($input: CreateBookInput!) {
		createBook(input: $input) {
			book @required {
				id
			}
		}
	}
`);

export const actions: Actions = {
	default: async (event): Promise<ActionFailure<{ form: Validation<typeof schema> }>> => {
		const data = await event.request.formData();
		const form = await superValidate(data, schema);

		if (!form.valid) {
			return fail(400, { form: form });
		}

		const cover = data.get('cover');

		if (!(cover instanceof File)) {
			return setError(form, 'cover', 'Cover is required');
		}

		const response = await addBook.mutate(
			{
				input: {
					...form.data,
					price: { raw: form.data.price },
					cover: { file: cover }
				}
			},
			{ event }
		);

		if (response.data?.createBook) {
			throw redirect(303, '/book/' + response.data.createBook.book.id);
		}

		return fail(400, { form: form });
	}
};
