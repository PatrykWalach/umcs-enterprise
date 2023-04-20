import { graphql } from '$houdini';
import BasketBook from '$lib/BasketBook.server';
import { error, type ServerLoad } from '@sveltejs/kit';
import type { Actions } from './$houdini';

export const load: ServerLoad = async ({ locals, params }) => {
	if (!params.id) {
		throw error(500, 'No id');
	}

	const data = await locals.client.request(
		graphql(/* GraphQL */ `
			query BookQuery($id: ID!) {
				node(id: $id) {
					__typename
					id
					... on Book {
						author
						covers(
							transformations: [
								{ width: 100 }
								{ width: 200 }
								{ width: 300 }
								{ width: 400 }
								{ width: 500 }
								{ width: 600 }
								{ width: 700 }
								{ width: 800 }
								{ width: 900 }
								{ width: 1000 }
								{ width: 1200 }
								{ width: 1400 }
								{ width: 1600 }
								{ width: 1800 }
								{ width: 2000 }
							]
						) {
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
	default: async ({ locals, request, cookies }) => {
		const { id } = Object.fromEntries(await request.formData());

		if (typeof id !== 'string') {
			throw error(500, 'No book id');
		}

		await BasketBook({ locals, cookies }, { id });

		return {};
	}
};
