import { graphql } from '$houdini';
import type { RequestEvent } from '../routes/$houdini';

const UnbasketBook = graphql(`
	mutation UnbasketBook($input: UnbasketBookInput!) {
		unbasketBook(input: $input) {
			basket {
				id
			}
		}
	}
`);

export default async function unbasketBook(event: RequestEvent, variables: { id: string }) {
	const basketId = event.cookies.get('basket');

	const response = await UnbasketBook.mutate(
		{
			input: {
				book: variables,
				...(basketId && {
					basket: { id: basketId }
				})
			}
		},
		{ event }
	);

	event.cookies.set('basket', response.data?.unbasketBook?.basket?.id ?? '', {
		path: '/'
	});
}
