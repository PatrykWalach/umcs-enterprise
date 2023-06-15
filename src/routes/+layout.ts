import { getSession } from '$houdini';
import { getBasket } from '$lib/setBasket';
import type { NavbarQueryVariables } from './$houdini';

export const _NavbarQueryVariables: NavbarQueryVariables = async (event) => {
	return {
		id: await getBasket(event)
	};
};
