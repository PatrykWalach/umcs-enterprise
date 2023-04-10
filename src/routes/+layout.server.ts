import { graphql } from '$gql';
import type { ServerLoad } from '@sveltejs/kit';

export const load: ServerLoad = async ({ locals, cookies }) => {
	const { data, error } = await locals.client
		.query(
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
		)
		.toPromise();

	cookies.set('basket', data?.basket?.id ?? '', {
		path: '/'
	});

	if (!data) {
		throw error;
	}

	return {
		NavbarQuery: data
	};
};
