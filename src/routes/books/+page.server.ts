import { graphql } from '$lib/gql';
import { Sort } from '$lib/gql/graphql';
import { type ServerLoad } from '@sveltejs/kit';

export const load: ServerLoad = ({ locals, request, url }) => {
	// 	<select name="by" id="" class="select">
	// 	<option value="" selected>Data dodania</option>
	// 	<option value="realease_date">Data wydania</option>
	// 	<option value="popularity">Popularność</option>
	// 	<option value="price">Cena</option>
	// </select>

	// <select name="order" id="" class="select">
	// 	<option value="asc">Rosnąco</option>
	// 	<option value="desc" selected>Malejąco</option>
	// </select>

	const order = {
		asc: Sort.Asc,
		desc: Sort.Desc
	}[url.searchParams.get('order') ?? ''];

	const sortBy = {
		realease_date: { releasedAt: order },
		popularity: { popularity: order },
		price: { price: order }
	}[url.searchParams.get('by') ?? ''] ?? { releasedAt: Sort.Desc };

	return locals.client.request(
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
			[url.searchParams.get('before') ? 'last' : 'first']: 50,
			sortBy
		}
	);
};
