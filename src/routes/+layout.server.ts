import { graphql } from '$lib/gql';
import type { ServerLoad } from '@sveltejs/kit';

export const load: ServerLoad = ({ locals }) => {
	return locals.client.request(
		graphql(/* GraphQL */ `
			query NavbarQuery {
				basket {
					totalPrice
					books(first: 10) {
						edges {
							cursor
						}
						pageInfo {
							hasNextPage
						}
					}
				}
			}
		`)
	);
};
