import BasketBook from '$lib/BasketBook.server';
import { error } from '@sveltejs/kit';
import type { Actions } from './$houdini';

export const actions: Actions = {
	default: async (event) => {
		const { id } = Object.fromEntries(await event.request.formData());

		if (typeof id !== 'string') {
			throw error(500, 'No book id');
		}

		await BasketBook({ input: { book: { id } } }, event);

		return {};
	}
};
