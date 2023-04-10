import { graphql } from '$lib/gql';
import type { ServerLoad } from '@sveltejs/kit';

export const load: ServerLoad = async ({ locals, cookies }) => {
	const data = await locals.client.request(
		graphql(/* GraphQL */ `
			query NavbarQuery($id: ID) {
				basket(id: $id) {
					books(first: 12) {
						edges {
							quantity
						}
						pageInfo {
							hasNextPage
						}
					}
					id
					price {
						formatted
					}
				}
				viewer {
					__typename
					id
				}
			}
		`),
		{
			id: cookies.get('basket')
		}
	);

	cookies.set('basket', data?.basket?.id ?? '', {
		path: '/'
	});

	return {
		NavbarQuery: data
	};
};
