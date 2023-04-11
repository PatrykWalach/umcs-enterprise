import { graphql } from '$gql';
import type { ServerLoad } from '@sveltejs/kit';

export const load: ServerLoad = async ({ locals, cookies }) => {
	const data = await locals.client.request(
		graphql(/* GraphQL */ `
			query NavbarQuery($id: ID) {
				basket(id: $id) {
					books(first: 3) {
						edges {
							node {
								covers(transformations: [{ width: 50 }]) {
									url
									width
								}
								id
								title
							}
						}
					}
					id
					price {
						formatted
					}
					quantity
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
