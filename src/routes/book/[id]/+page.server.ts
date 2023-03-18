import { graphql } from '$lib/gql';
import type { ServerLoad } from '@sveltejs/kit';

export const load: ServerLoad = async ({ locals, params }) => {
	if (!params.id) {
		throw new Error('No id');
	}

	const data = await locals.client.request(
		graphql(/* GraphQL */ `
			query BookQuery($id: ID!) {
				node(id: $id) {
					__typename
					id
					... on Book {
						author
						cover {
							height
							url
							width
						}
						price
						recommended(first: 5) {
							edges {
								node {
									...Book_book
									id
								}
							}
						}
						synopsis
						title
					}
				}
			}
		`),
		{
			id: params.id
		}
	);

	if (data.node?.__typename !== 'Book') {
		throw new Error('Not a Book');
	}

	return {
		...data,
		node: data.node
	};
};
