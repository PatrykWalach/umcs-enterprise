import type { BasketQueryVariables } from './$types';

export const _BasketQueryVariables: BasketQueryVariables = async (event) => {
	return {
		after: event.url.searchParams.get('after')
	};
};
