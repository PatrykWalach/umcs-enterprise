import { graphql } from '$gql';
import type { Cookies } from '@sveltejs/kit';

const UnbasketBook = graphql(`
	mutation UnbasketBook($input: UnbasketBookInput!) {
		unbasketBook(input: $input) {
			basket {
				id
			}
		}
	}
`);

export default async function unbasketBook(
	event: { locals: App.Locals; cookies: Cookies },
	variables: { id: string }
) {
	const basketId = event.cookies.get('basket');

	const data = await event.locals.client.mutation(UnbasketBook, {
		input: {
			book: variables,
			...(basketId && {
				basket: { id: basketId }
			})
		}
	});

	event.cookies.set('basket', data.unbasketBook?.basket?.id ?? '', {
		path: '/'
	});
}
