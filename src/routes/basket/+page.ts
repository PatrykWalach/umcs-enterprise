import { getBasket } from '$lib/setBasket';
import { redirect } from '@sveltejs/kit';
import type { AfterLoadEvent, BasketQueryVariables } from './$houdini';

export const _BasketQueryVariables: BasketQueryVariables = async (event) => {
	return {
		after: event.url.searchParams.get('after'),
		before: event.url.searchParams.get('before'),
		[event.url.searchParams.get('before') ? 'last' : 'first']: 8,
		id: await getBasket(event)
	};
};

export const _houdini_afterLoad = ({ data, input }: AfterLoadEvent) => {
	if (
		(input.BasketQuery.after ?? input.BasketQuery.before) &&
		data.BasketQuery.basket?.books?.edges?.length === 0
	) {
		throw redirect(303, '/basket');
	}
};
