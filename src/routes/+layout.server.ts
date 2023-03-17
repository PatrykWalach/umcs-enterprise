import { graphql } from '$lib/gql';
import type { ServerLoad } from '@sveltejs/kit';

export const load: ServerLoad = ({ locals }) => {
	return locals.client.request(
		graphql(/* GraphQL */ `
			query LayoutQuery {
				books(first: 10) {
					edges {
						node {
							id
							title
							cover {
								url
								width
								height
							}
						}
					}
				}
			}
		`)
	);
};
