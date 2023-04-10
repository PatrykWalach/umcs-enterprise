import { graphql } from '$gql';
import type { Cookies } from '@sveltejs/kit';

const BasketBook = graphql(`
	mutation BasketBook($input: BasketBookInput!) {
		basketBook(input: $input) {
			basket {
				id
			}
		}
	}
`);

export default async function basketBook(
	event: { locals: App.Locals; cookies: Cookies },
	variables: { id: string }
) {
	const basketId = event.cookies.get('basket');

	const data = await event.locals.client.request(BasketBook, {
		input: {
			book: variables,
			...(basketId && {
				basket: { id: basketId }
			})
		}
	});

	event.cookies.set('basket', data.basketBook?.basket?.id ?? '', {
		path: '/'
	});
}
