import { graphql } from '$gql';
import type { Actions } from '@sveltejs/kit';

export const actions: Actions = {
	default: async ({ request, locals }) => {
		const data = await request.formData();
		const { cover, author, price, title } = Object.fromEntries(data);

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
					price: Number(price),
					cover: cover instanceof File ? cover : null
				}
			},
			{
				'graphql-require-preflight': ''
			}
		);

		return {};
	}
};
