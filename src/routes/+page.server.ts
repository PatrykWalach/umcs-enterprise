import { graphql } from '$lib/gql';
import type { Actions } from '@sveltejs/kit';

export const actions: Actions = {
	default: async ({ request, locals }) => {
		const data = await request.formData();

		console.log(data);

		await locals.client.request(
			graphql(`
				mutation AddBook($input: CreateBookInput!, $cover: Upload) {
					createBook(input: $input, cover: $cover) {
						id
					}
				}
			`),
			{
				input: {
					title: String(data.get('title')),
					author: String(data.get('author')),
					price: Number(data.get('price'))
				},
				cover: data.get('cover')
			}
		);
	}
};
