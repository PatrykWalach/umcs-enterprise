import { graphql } from '$gql';
import { Order } from '$gql/graphql';
import BasketBook from '$lib/BasketBook.server';
import type { ServerLoad } from '@sveltejs/kit';
import { error } from '@sveltejs/kit';
import type { Actions } from './$types';

export const load: ServerLoad = ({ locals, url }) => {
	const purchase = {
		asc: Order.Asc,
		desc: Order.Desc
	}[url.searchParams.get('purchase') ?? ''];

	const orderBy = {
		realease_date: { releasedAt: purchase },
		popularity: { popularity: purchase },
		price: { price: { raw: purchase } }
	}[url.searchParams.get('by') ?? ''] ?? { releasedAt: Order.Desc };

	return {
		BooksQuery: locals.client.request(
			graphql(/* GraphQL */ `
				query BooksQuery(
					$after: String
					$before: String
					$first: Int
					$last: Int
					$orderBy: [BookOrderBy]
				) {
					books(after: $after, before: $before, first: $first, last: $last, orderBy: $orderBy) {
						edges {
							node {
								...Book_book
								id
							}
						}
						pageInfo {
							endCursor
							hasNextPage
							hasPreviousPage
							startCursor
						}
					}
				}
			`),
			{
				after: url.searchParams.get('after'),
				before: url.searchParams.get('before'),
				[url.searchParams.get('before') ? 'last' : 'first']: 60,
				orderBy
			}
		)
	};
};

export const actions: Actions = {
	default: async ({ locals, request, cookies }) => {
		const { id } = Object.fromEntries(await request.formData());

		if (typeof id !== 'string') {
			throw error(500,'No book id');
		}

		await BasketBook(
			{
				locals,
				cookies
			},
			{ id }
		);

		return {};
	}
};
