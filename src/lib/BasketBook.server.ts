import { graphql, type BasketBook$input, setSession } from '$houdini';
import type { RequestEvent } from '@sveltejs/kit';

const BasketBook = graphql(`
	mutation BasketBook($input: BasketBookInput!) {
		basketBook(input: $input) {
			token
		}
	}
`);

export default async function basketBook(variables: BasketBook$input, event: RequestEvent) {
	const response = await BasketBook.mutate(variables, {
		event
	});

	const token = response.data?.basketBook?.token || '';

	event.cookies.set('enterprise-token', token, {
		path: '/'
	});

	setSession(event, {
		token
	});

	return response;
}
