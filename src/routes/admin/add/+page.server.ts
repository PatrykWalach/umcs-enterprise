import { graphql } from '$gql';
import type { Actions } from '@sveltejs/kit';

export const actions: Actions = {
	default: async ({ request, locals }) => {
		const data = await request.formData();
		const { cover, author, price, title, url, databaseId, releasedAt } = Object.fromEntries(data);

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
					releasedAt: String(releasedAt),
					databaseId: String(databaseId)
				}
			},
			{
				'graphql-require-preflight': ''
			}
		);

		return {};
	}
};
