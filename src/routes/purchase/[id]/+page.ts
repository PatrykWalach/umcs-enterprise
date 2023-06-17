import { error } from '@sveltejs/kit';
import type { AfterLoadEvent } from './$houdini';

export const _houdini_afterLoad = (event: AfterLoadEvent) => {
	if (event.data.PurchaseQuery.node?.__typename !== 'Purchase') {
		throw error(404);
	}
};
