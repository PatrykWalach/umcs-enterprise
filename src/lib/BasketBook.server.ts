import type { RequestEvent } from '@sveltejs/kit';
import { redirect } from 'sveltekit-flash-message/server';
import { setToken } from './setToken';
function graphql() {}
const BasketBook = graphql(`
	mutation BasketBook($input: BasketBookInput!) {
		basketBook(input: $input) {
			token {
				...SetToken_token @mask_disable
			}
		}
	}
`);

export default async function basketBook(variables: BasketBook$input, event: RequestEvent) {
	const response = await BasketBook.mutate(variables, {
		event
	});

	const token = response.data?.basketBook?.token;
	setToken(event, token);

	if (response.data?.basketBook) {
		throw redirect({ type: 'BASKET_BOOK' }, event);
	}

	return response;
}
