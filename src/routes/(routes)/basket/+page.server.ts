import { setSession } from '$houdini';
import BasketBook from '$lib/BasketBook.server';
import UnbasketBook from '$lib/UnbasketBook.server';
import { BASKET_COOKIE, TOKEN_COOKIE } from '$lib/constants';
import { error, redirect } from '@sveltejs/kit';
import type { Actions } from './$houdini';

export const actions: Actions = {
	basket_book: async (event) => {
		const { id } = Object.fromEntries(await event.request.formData());
		if (typeof id !== 'string') {
			throw error(500, 'No book id');
		}

		await BasketBook({ input: { book: { id } } }, event);

		return redirect(303, '/basket');
	},
	unbasket_book: async (event) => {
		const { id } = Object.fromEntries(await event.request.formData());
		if (typeof id !== 'string') {
			throw new Error('No book id');
		}

		await UnbasketBook({ input: { book: { id } } }, event);

		return redirect(303, '/basket');
	},
	clear_basket: async (event) => {
		event.cookies.delete(BASKET_COOKIE);
		setSession(event, {
			token: event.cookies.get(TOKEN_COOKIE),
			basket: undefined
		});

		return redirect(303, '/basket');
	}
};
