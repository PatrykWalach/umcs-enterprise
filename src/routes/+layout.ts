import type { AfterLoadEvent, NavbarQueryVariables } from './$houdini';

export const _NavbarQueryVariables: NavbarQueryVariables = (event) => {
	
	return {
		id: event.cookies?.get('basket')
	};
};

export const _houdini_afterLoad = async (event: AfterLoadEvent) => {
	event.cookies?.set('basket', event.data.NavbarQuery?.basket?.id ?? '', {
		path: '/'
	});
};
