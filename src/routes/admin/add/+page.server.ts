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
			const errors = input.error.format();
			return fail(403, {
				releasedAt: { value: data.releasedAt, errors: errors.releasedAt?._errors },
				title: { value: data.title, errors: errors.title?._errors },
				author: { value: data.author, errors: errors.author?._errors },
				price: { value: data.price, errors: errors.price?._errors },
				cover: { value: null, errors: errors.cover?._errors }
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
			releasedAt: { value: data.releasedAt, errors: undefined },
			title: { value: data.title, errors: undefined },
			author: { value: data.author, errors: undefined },
			price: { value: data.price, errors: undefined },
			cover: { value: null, errors: undefined }
		});
	}
};
