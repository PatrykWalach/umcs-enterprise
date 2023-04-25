import { graphql } from '$houdini';
import { fail, redirect, type Actions } from '@sveltejs/kit';
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
					book {
						id
					}
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

		if (result.data?.createBook?.book?.id) {
			throw redirect(303, `/book/${result.data.createBook.book.id}`);
		}

		return fail(403, {
			data: {
				releasedAt: data.releasedAt,
				title: data.title,
				author: data.author,
				price: data.price
			},
			// internal error?
			errors: {}
		});
	}
};
