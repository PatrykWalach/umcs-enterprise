import BasketBook from '$lib/BasketBook.server';
import UnbasketBook from '$lib/UnbasketBook.server';
import { error } from '@sveltejs/kit';
import type { Actions } from './$houdini';

export const actions: Actions = {
	basket_book: async (event) => {
		const { id } = Object.fromEntries(await event.request.formData());
		if (typeof id !== 'string') {
			throw error(500, 'No book id');
		}

		const data = await BasketBook(event, { id });

		return {};
	},
	unbasket_book: async (event) => {
		const { id } = Object.fromEntries(await event.request.formData());
		if (typeof id !== 'string') {
			throw new Error('No book id');
		}

		const data = await UnbasketBook(event, { id });

		return {};
	}
};
