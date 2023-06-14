import { graphql, type BasketBook$input } from '$houdini';
import { redirect, type RequestEvent } from '@sveltejs/kit';

import { setToken } from './setToken';

const BasketBook = graphql(`
	mutation BasketBook($input: BasketBookInput!) {
		basketBook(input: $input) {
			edge {
				node {
					id
				}
			}
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

	if (response.data?.basketBook?.edge?.node?.id) {
		throw redirect(303, '/book/' + response.data.basketBook.edge.node.id);
	}

	return response;
}
