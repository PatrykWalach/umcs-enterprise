import type { Cookies } from '@sveltejs/kit';
import { graphql } from './gql/index';

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

	const data = await event.locals.client.request(UnbasketBook, {
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
