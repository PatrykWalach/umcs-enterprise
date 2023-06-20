import type { PurchasesQueryVariables } from './$houdini';

export const _PurchasesQueryVariables: PurchasesQueryVariables = (event) => {
	return {
		after: event.url.searchParams.get('after'),
		before: event.url.searchParams.get('before'),
		[event.url.searchParams.get('before') ? 'last' : 'first']: 8
	};
};
