import { graphql } from '$lib/gql';
import type { ServerLoad } from '@sveltejs/kit';

export const load: ServerLoad = ({ locals }) => {
	return locals.client.request(
		graphql(/* GraphQL */ `
			query LayoutQuery {
				popular: books(first: 10, sortBy: { popularity: DESC }) {
					edges {
						node {
							id
							...Book_book
						}
					}
				}
				new: books(first: 10, sortBy: { releasedAt: DESC }) {
					edges {
						node {
							id
							...Book_book
						}
					}
				}
			}
		`)
	);
};
