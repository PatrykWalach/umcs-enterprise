import { graphql, setSession, type UnbasketBook$input } from '$houdini';
import type { RequestEvent } from '@sveltejs/kit';

const UnbasketBook = graphql(`
	mutation UnbasketBook($input: UnbasketBookInput!) {
		unbasketBook(input: $input) {
			token
		}
	}
`);

export default async function unbasketBook(variables: UnbasketBook$input, event: RequestEvent) {
	const response = await UnbasketBook.mutate(variables, { event });

	const token = response.data?.unbasketBook?.token || ''

	event.cookies.set('enterprise-token', token, {
		path: '/'
	});

	setSession(event, {
		token
	})

	return response;
}
