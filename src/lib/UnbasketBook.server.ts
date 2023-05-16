import { graphql, type UnbasketBook$input } from '$houdini';
import type { RequestEvent } from '@sveltejs/kit';
import { setToken } from './setToken';

const UnbasketBook = graphql(`
	mutation UnbasketBook($input: UnbasketBookInput!) {
		unbasketBook(input: $input) {
			token {
				...SetToken_token @mask_disable
			}
		}
	}
`);

export default async function unbasketBook(variables: UnbasketBook$input, event: RequestEvent) {
	const response = await UnbasketBook.mutate(variables, { event });

	setToken(event, response.data?.unbasketBook?.token);

	return response;
}
