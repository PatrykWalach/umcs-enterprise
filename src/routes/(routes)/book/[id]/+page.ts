import { getSession } from '$houdini';
import type { LoadEvent } from '@sveltejs/kit';
import type { BookQueryVariables } from './$houdini';
import { getBasket } from '$lib/setBasket';

export const _BookQueryVariables: BookQueryVariables = async (event) => {
	return {
		basketId: await getBasket(event)
	};
};
