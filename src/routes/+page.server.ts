import { graphql } from '$lib/gql';
import type { ServerLoad } from '@sveltejs/kit';

export const load: ServerLoad = ({ locals }) => {
	return locals.client.request(
		graphql(/* GraphQL */ `
			query LayoutQuery {
				new: books(first: 10, sortBy: { releasedAt: DESC }) {
					edges {
						node {
							...Book_book
							id
						}
					}
				}
				popular: books(first: 10, sortBy: { popularity: DESC }) {
					edges {
						node {
							...Book_book
							id
						}
					}
				}
			}
		`)
	);
};
