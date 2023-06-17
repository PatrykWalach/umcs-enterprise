import BasketBook from '$lib/BasketBook.server';
import { error, redirect } from '@sveltejs/kit';
import type { Actions } from './$houdini';

export const actions: Actions = {
	basket_book: async (event) => {
		const { id } = Object.fromEntries(await event.request.formData());

		if (typeof id !== 'string') {
			throw error(500, 'No book id');
		}

		const response = await BasketBook({ input: { book: { id } } }, event);

		if (response.data?.basketBook?.edge?.node?.id) {
			throw redirect(303, '/book/' + response.data.basketBook.edge.node.id);
		}
	}
};
