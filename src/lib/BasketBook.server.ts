import { graphql, type BasketBook$input } from '$houdini';
import type { RequestEvent } from '@sveltejs/kit';
import { setToken } from './setToken';

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

	return response;
}
