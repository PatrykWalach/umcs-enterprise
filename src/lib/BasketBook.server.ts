import { graphql, type BasketBook$input } from '$houdini';
import { redirect, type RequestEvent } from '@sveltejs/kit';

import { setBasket } from './setBasket';
import { BASKET_COOKIE } from './constants';

const BasketBook = graphql(`
	mutation BasketBook($input: BasketBookInput!) {
		basketBook(input: $input) {
			basket @required {
				id
			}
			edge {
				node {
					id
				}
			}
		}
	}
`);

export default async function basketBook(variables: BasketBook$input, event: RequestEvent) {
	const response = await BasketBook.mutate(
		{
			input: { book: variables.input.book, basket: { id: event.cookies.get(BASKET_COOKIE) } }
		},
		{
			event
		}
	);

	if (response.data?.basketBook) {
		setBasket(event, response.data.basketBook.basket.id);
	}

	if (response.data?.basketBook?.edge?.node?.id) {
		throw redirect(303, '/book/' + response.data.basketBook.edge.node.id);
	}

	return response;
}
