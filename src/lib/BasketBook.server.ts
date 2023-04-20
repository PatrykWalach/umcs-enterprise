import { graphql } from '$houdini';
import type { RequestEvent } from '@sveltejs/kit';

const BasketBook = graphql(`
	mutation BasketBook($input: BasketBookInput!) {
		basketBook(input: $input) {
			basket {
				id
			}
		}
	}
`);

export default async function basketBook(event: RequestEvent, variables: { id: string }) {
	const basketId = event.cookies.get('basket');

	const response = await BasketBook.mutate(
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

	event.cookies.set('basket', response.data?.basketBook?.basket?.id ?? '', {
		path: '/'
	});
}
