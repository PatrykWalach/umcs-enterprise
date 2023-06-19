import { error } from '@sveltejs/kit';
import type { AfterLoadEvent, PurchaseQueryVariables } from './$houdini';

export const _houdini_afterLoad = (event: AfterLoadEvent) => {
	if (event.data.PurchaseQuery.node?.__typename !== 'Purchase') {
		throw error(404);
	}
};

export const _PurchaseQueryVariables: PurchaseQueryVariables = async (event) => {
	return {
		after: event.url.searchParams.get('after'),
		before: event.url.searchParams.get('before'),
		[event.url.searchParams.get('before') ? 'last' : 'first']: 8
	};
};
