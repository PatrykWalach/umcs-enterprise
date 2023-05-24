import BasketBook from '$lib/BasketBook.server';
import UnbasketBook from '$lib/UnbasketBook.server';
import { error } from '@sveltejs/kit';
import type { Actions } from './$types';

export const actions: Actions = {
	basket_book: async (event) => {
		const { id } = Object.fromEntries(await event.request.formData());
		if (typeof id !== 'string') {
			throw error(500, 'No book id');
		}

		await BasketBook({ input: { book: { id } } }, event);

		return {};
	},
	unbasket_book: async (event) => {
		const { id } = Object.fromEntries(await event.request.formData());
		if (typeof id !== 'string') {
			throw new Error('No book id');
		}

		await UnbasketBook({ input: { book: { id } } }, event);

		return {};
	}
};
