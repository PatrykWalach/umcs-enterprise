import { BasketQueryStore } from '$houdini';
import BasketBook from '$lib/BasketBook.server';
import UnbasketBook from '$lib/UnbasketBook.server';
import { error } from '@sveltejs/kit';
import type { Actions } from './$houdini';

export const actions: Actions = {
	basket_book: async ({ locals, request, cookies }) => {
		const { id } = Object.fromEntries(await request.formData());
		if (typeof id !== 'string') {
			throw error(500, 'No book id');
		}

		const basketId = cookies.get('basket');

		const data = await BasketBook({ locals, cookies }, { id });

		return {};
	},
	unbasket_book: async ({ locals, request, cookies }) => {
		const { id } = Object.fromEntries(await request.formData());
		if (typeof id !== 'string') {
			throw new Error('No book id');
		}

		const data = await UnbasketBook({ locals, cookies }, { id });

		return {};
	}
};
