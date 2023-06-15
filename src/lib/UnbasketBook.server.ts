import { graphql, type UnbasketBook$input } from '$houdini';
import type { RequestEvent } from '@sveltejs/kit';
import { setBasket } from './setBasket';
import { BASKET_COOKIE } from './constants';

const UnbasketBook = graphql(`
	mutation UnbasketBook($input: UnbasketBookInput!) {
		unbasketBook(input: $input) {
			basket @required {
				id
			}
		}
	}
`);

export default async function unbasketBook(variables: UnbasketBook$input, event: RequestEvent) {
	const response = await UnbasketBook.mutate(
		{
			input: { book: variables.input.book, basket: { id: event.cookies.get(BASKET_COOKIE) } }
		},
		{ event }
	);

	if (response.data?.unbasketBook) {
		setBasket(event, response.data.unbasketBook.basket.id);
	}

	return response;
}
