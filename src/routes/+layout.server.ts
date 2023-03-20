import { graphql } from '$lib/gql';
import type { ServerLoad } from '@sveltejs/kit';

export const load: ServerLoad = ({ locals }) => {
	return {
		NavbarQuery: locals.client.request(
			graphql(/* GraphQL */ `
				query NavbarQuery {
					basket {
						books(first: 12) {
							edges {
								quantity
							}
							pageInfo {
								hasNextPage
							}
						}
						totalPrice
					}
				}
			`)
		)
	};
};
