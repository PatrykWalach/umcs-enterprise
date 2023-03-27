import BasketBook from '$lib/BasketBook';
import { graphql } from '$lib/gql';
import UnbasketBook from '$lib/UnbasketBook';
import type { ServerLoad } from '@sveltejs/kit';
import type { Actions } from './$types';

export const load: ServerLoad = ({ locals, url }) => {
	return {
		BasketQuery: locals.client.request(
			graphql(/* GraphQL */ `
				query BasketQuery($after: String) {
					basket {
						books(after: $after, first: 10) {
							edges {
								node {
									author
									cover {
										height
										id
										url
										width
									}
									id
									title
								}
								price {
									formatted
								}
								quantity
							}
							pageInfo {
								endCursor
								hasNextPage
							}
						}
						price {
							formatted
						}
					}
				}
			`),
			{
				after: url.searchParams.get('after')
			}
		)
	};
};

export const actions: Actions = {
	basket_book: async ({ locals, request }) => {
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
	},
	unbasket_book: async ({ locals, request }) => {
		const { id } = Object.fromEntries(await request.formData());
		if (typeof id !== 'string') {
			throw new Error('No book id');
		}
		await locals.client.request(UnbasketBook, {
			input: {
				id
			}
		});

		return {};
	}
};
