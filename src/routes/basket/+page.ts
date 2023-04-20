import type { BasketQueryVariables } from "./$houdini";

export const _BasketQueryVariables: BasketQueryVariables = async (event) => {
 

	return {
		after: event.url.searchParams.get('after'),
		id: event.cookies?.get('basket')
	}
};