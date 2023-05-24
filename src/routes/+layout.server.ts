import { loadFlashMessage } from 'sveltekit-flash-message/server';
import type { LayoutServerLoad } from './$types';

export const load = loadFlashMessage(async (event) => {
	return {
		NavbarQuery: {
			data: null
		}
	};
}) satisfies LayoutServerLoad;
