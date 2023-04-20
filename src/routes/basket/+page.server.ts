import { graphql } from '$gql';
import { BasketQueryStore } from '$houdini';
import BasketBook from '$lib/BasketBook.server';
import UnbasketBook from '$lib/UnbasketBook.server';
import { error, type ServerLoad } from '@sveltejs/kit';
import type { Actions } from './$types';

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
`);
export const load: ServerLoad = async (event) => {
	const { data } = await new BasketQueryStore().fetch({
		event,
		variables: {
			after: event.url.searchParams.get('after'),
			id: event.cookies.get('basket')
		}
	});

	return {
		BasketQuery: data
	};
};

export const actions: Actions = {
	basket_book: async ({ locals, request, cookies }) => {
		const { id } = Object.fromEntries(await request.formData());
		if (typeof id !== 'string') {
			throw error(500, 'No book id');
		}

		const basketId = cookies.get('basket');

		const data = await BasketBook({ locals, cookies }, { id });

		return {};
	},
	unbasket_book: async ({ locals, request, cookies }) => {
		const { id } = Object.fromEntries(await request.formData());
		if (typeof id !== 'string') {
			throw new Error('No book id');
		}

		const data = await UnbasketBook({ locals, cookies }, { id });

		return {};
	}
};
