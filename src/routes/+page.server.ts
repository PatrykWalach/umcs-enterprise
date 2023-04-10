import { graphql } from '$gql';
import BasketBook from '$lib/BasketBook';
import type { ServerLoad } from '@sveltejs/kit';
import type { Actions } from './$types';

export const load: ServerLoad = ({ locals }) => {
	return {
		HomeQuery: locals.client.request(
			graphql(/* GraphQL */ `
				query HomeQuery {
					new: books(first: 12, sortBy: { releasedAt: DESC }) {
						edges {
							node {
								...Book_book
								id
							}
						}
					}
					popular: books(first: 12, sortBy: { popularity: DESC }) {
						edges {
							node {
								...Book_book
								id
							}
						}
					}
				}
			`)
		)
	};
};

export const actions: Actions = {
	default: async ({ locals, request }) => {
		const { id } = Object.fromEntries(await request.formData());

		if (typeof id !== 'string') {
			throw new Error('No book id');
		}

		await locals.client.request(BasketBook, {
			input: {
				id
			}
		});

		return {};
	}
};
