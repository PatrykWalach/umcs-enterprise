import BasketBook from '$lib/BasketBook.server';
import { graphql } from '$lib/gql';
import UnbasketBook from '$lib/UnbasketBook.server';
import type { ServerLoad } from '@sveltejs/kit';
import type { Actions } from './$types';

export const load: ServerLoad = ({ locals, url, cookies }) => {
	return {
		BasketQuery: locals.client.request(
			graphql(/* GraphQL */ `
				query BasketQuery($after: String, $id: ID) {
					basket(id: $id) {
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
						id
						price {
							formatted
						}
					}
				}
			`),
			{
				after: url.searchParams.get('after'),
				id: cookies.get('basket')
			}
		)
	};
};

export const actions: Actions = {
	basket_book: async ({ locals, request, cookies }) => {
		const { id } = Object.fromEntries(await request.formData());
		if (typeof id !== 'string') {
			throw new Error('No book id');
		}

		const basketId = cookies.get('basket');

		const data = await BasketBook({ locals, cookies }, { id });

		return {};
	},
	unbasket_book: async ({ locals, request, cookies }) => {
		const { id } = Object.fromEntries(await request.formData());
		if (typeof id !== 'string') {
			throw new Error('No book id');
		}

		const data = await UnbasketBook({ locals, cookies }, { id });

		return {};
	}
};
