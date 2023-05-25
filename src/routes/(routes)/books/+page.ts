import { Order } from '$houdini';
import type { BooksQueryVariables } from './$houdini';

export const _BooksQueryVariables: BooksQueryVariables = (event) => {
	const order = {
		asc: Order.ASC,
		desc: Order.DESC
	}[event.url.searchParams.get('order') ?? ''];

	const orderBy = {
		realease_date: { releasedAt: order },
		popularity: { popularity: order },
		price: { price: { raw: order } }
	}[event.url.searchParams.get('by') ?? ''] ?? { releasedAt: Order.DESC };

	return {
		after: event.url.searchParams.get('after'),
		before: event.url.searchParams.get('before'),
		[event.url.searchParams.get('before') ? 'last' : 'first']: 60,
		orderBy: [orderBy]
	};
};
