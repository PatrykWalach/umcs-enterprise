import { graphql } from '$gql';
import { NavbarQueryStore } from '$houdini';
import type { ServerLoad } from '@sveltejs/kit';

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
`);

export const load: ServerLoad = async (event) => {
	const { data } = await new NavbarQueryStore().fetch({
		event,
		variables: {
			id: event.cookies.get('basket')
		}
	});

	event.cookies.set('basket', data?.basket?.id ?? '', {
		path: '/'
	});

	return {
		NavbarQuery: data
	};
};
