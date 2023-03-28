import { graphql } from '$lib/gql';
import type { Actions } from '@sveltejs/kit';

export const actions: Actions = {
	default: async ({ request, locals }) => {
		const data = await request.formData();
		const { cover, author, price, title, releasedAt } = Object.fromEntries(data);

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
					cover: cover instanceof File ? cover : null,
					releasedAt: String(releasedAt)
				}
			},
			{
				'graphql-require-preflight': ''
			}
		);

		return {};
	}
};
