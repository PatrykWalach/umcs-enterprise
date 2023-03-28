import BasketBook from '$lib/BasketBook.server';
import { graphql } from '$lib/gql';
import type { ServerLoad } from '@sveltejs/kit';
import type { Actions } from './$types';

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
							id
							url
							width
						}
						price {
							formatted
						}
						recommended(first: 6) {
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
		BookQuery: {
			...data,
			node: data.node
		}
	};
};

export const actions: Actions = {
	default: async ({ locals, params, cookies }) => {
		await BasketBook({ locals, cookies }, { id: params.id });

		return {};
	}
};
