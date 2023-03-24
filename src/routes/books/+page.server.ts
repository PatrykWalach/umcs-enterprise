import BasketBook from '$lib/BasketBook';
import { graphql } from '$lib/gql';
import { Sort } from '$lib/gql/graphql';
import type { ServerLoad } from '@sveltejs/kit';
import type { Actions } from './$types';

export const load: ServerLoad = ({ locals, url }) => {
	const order = {
		asc: Sort.Asc,
		desc: Sort.Desc
	}[url.searchParams.get('order') ?? ''];

	const sortBy = {
		realease_date: { releasedAt: order },
		popularity: { popularity: order },
		price: { price: order }
	}[url.searchParams.get('by') ?? ''] ?? { releasedAt: Sort.Desc };

	return {
		BooksQuery: locals.client.request(
			graphql(/* GraphQL */ `
				query BooksQuery(
					$after: String
					$before: String
					$first: Int
					$last: Int
					$sortBy: [BookSortBy]
				) {
					books(after: $after, before: $before, first: $first, last: $last, sortBy: $sortBy) {
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
				sortBy
			}
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
