import { graphql } from '$gql';
import BasketBook from '$lib/BasketBook.server';
import UnbasketBook from '$lib/UnbasketBook.server';
import type { ServerLoad } from '@sveltejs/kit';
import type { Actions } from './$types';

export const load: ServerLoad = async ({ locals, url, cookies }) => {
	const { data, error } = await locals.client.query(
		graphql(/* GraphQL */ `
			query BasketQuery($after: String, $id: ID) {
				basket(id: $id) {
					books(after: $after, first: 10) {
						edges {
							node {
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
								id
								title
							}
							price {
								formatted
							}
							quantity
						}
						pageInfo {
							endCursor
							hasNextPage
						}
					}
					id
					price {
						formatted
					}
				}
			}
		`),
		{
			after: url.searchParams.get('after'),
			id: cookies.get('basket')
		}
	);

	if (!data) {
		throw error;
	}

	return {
		BasketQuery: data
	};
};

export const actions: Actions = {
	basket_book: async ({ locals, request, cookies }) => {
		const { id } = Object.fromEntries(await request.formData());
		if (typeof id !== 'string') {
			throw new Error('No book id');
		}

		await BasketBook({ locals, cookies }, { id });

		return {};
	},
	unbasket_book: async ({ locals, request, cookies }) => {
		const { id } = Object.fromEntries(await request.formData());
		if (typeof id !== 'string') {
			throw new Error('No book id');
		}

		await UnbasketBook({ locals, cookies }, { id });

		return {};
	}
};
