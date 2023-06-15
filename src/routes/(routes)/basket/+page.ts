import { getBasket } from '$lib/setBasket';
import type { BasketQueryVariables } from './$houdini';

export const _BasketQueryVariables: BasketQueryVariables = async (event) => {
	return {
		after: event.url.searchParams.get('after'),
		id: await getBasket(event)
	};
};
