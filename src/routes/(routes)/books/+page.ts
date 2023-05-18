import { Order } from '$houdini';
import type { BooksQueryVariables } from './$houdini';

export const _BooksQueryVariables: BooksQueryVariables = (event) => {
	const purchase = {
		asc: Order.ASC,
		desc: Order.DESC
	}[event.url.searchParams.get('purchase') ?? ''];

	const orderBy = {
		realease_date: { releasedAt: purchase },
		popularity: { popularity: purchase },
		price: { price: { raw: purchase } }
	}[event.url.searchParams.get('by') ?? ''] ?? { releasedAt: Order.DESC };

	return {
		after: event.url.searchParams.get('after'),
		before: event.url.searchParams.get('before'),
		[event.url.searchParams.get('before') ? 'last' : 'first']: 60,
		orderBy: [orderBy]
	};
};
