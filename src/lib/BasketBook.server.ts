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

	const { data, error } = await event.locals.client.mutation(BasketBook, {
		input: {
			book: variables,
			...(basketId && {
				basket: { id: basketId }
			})
		}
	});

	if (!data) {
		throw error;
	}

	event.cookies.set('basket', data.basketBook?.basket?.id ?? '', {
		path: '/'
	});
}
