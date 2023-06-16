import { error } from '@sveltejs/kit';
import type { AfterLoadEvent } from './$houdini';

export const _houdini_afterLoad = (event: AfterLoadEvent) => {
	if (event.data.BookQuery.node?.__typename !== 'Book') {
		throw error(404);
	}
};

import { getBasket } from '$lib/setBasket';
import type { BookQueryVariables } from './$houdini';

export const _BookQueryVariables: BookQueryVariables = async (event) => {
	return {
		basketId: await getBasket(event)
	};
};
