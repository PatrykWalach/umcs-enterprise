import { graphql, type BasketBook$input } from '$houdini';
import { type RequestEvent } from '@sveltejs/kit';

import { BASKET_COOKIE } from './constants';
import { setBasket } from './setBasket';

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

	return response;
}
