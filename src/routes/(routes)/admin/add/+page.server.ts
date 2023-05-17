import { graphql } from '$houdini';
import { fail } from '@sveltejs/kit';
import type { Actions } from './$houdini';
import { z } from 'zod';

const Input = z.object({
	releasedAt: z.coerce.date(),
	title: z.string(),
	author: z.string(),
	price: z.coerce.number().safe().positive(),
	cover: z.instanceof(File)
});

export const actions: Actions = {
	default: async (event) => {
		const data = Object.fromEntries(await event.request.formData());
		const input = Input.safeParse(data);

		if (!input.success) {
			return fail(403, {
				data: {
					releasedAt: data.releasedAt,
					title: data.title,
					author: data.author,
					price: data.price
				},
				errors: input.error.format()
			});
		}

		const result = await graphql(`
			mutation AddBook($input: CreateBookInput!) {
				createBook(input: $input) {
					__typename
				}
			}
		`).mutate(
			{
				input: {
					title: input.data.title,
					author: input.data.author,
					price: { raw: input.data.price },
					cover: {
						file: input.data.cover
					},
					releasedAt: input.data.releasedAt
				}
			},
			{ event }
		);

		console.log(result);
	}
};
