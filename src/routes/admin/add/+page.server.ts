import { graphql } from '$gql';
import type { Actions } from '@sveltejs/kit';

export const actions: Actions = {
	default: async ({ request, locals }) => {
		const data = await request.formData();
		const { cover, author, price, title, url } = Object.fromEntries(data);

		if (!(cover instanceof File) || !(typeof url === 'string')) {
			throw new Error('cover not uploaded');
		}

		await locals.client.request(
			graphql(`
				mutation AddBook($input: CreateBookInput!) {
					createBook(input: $input) {
						__typename
					}
				}
			`),
			{
				input: {
					title: String(title),
					author: String(author),
					price: { raw: Number(price) },
					cover: {
						file: cover,
						url
					},
					releasedAt: new Date().toISOString()
				}
			},
			{
				'graphql-require-preflight': ''
			}
		);

		return {};
	}
};
