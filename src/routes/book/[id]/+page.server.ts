import { graphql } from '$houdini';
import BasketBook from '$lib/BasketBook.server';
import { error, redirect } from '@sveltejs/kit';
import type { Actions } from './$houdini';

export const actions: Actions = {
	basket_book: async (event) => {
		const { id } = Object.fromEntries(await event.request.formData());

		if (typeof id !== 'string') {
			throw error(500, 'No book id');
		}

		await BasketBook({ input: { book: { id } } }, event);

		throw redirect(303, '/book/' + id);
	},
	delete: async (event) => {
		const { id } = Object.fromEntries(await event.request.formData());

		if (typeof id !== 'string') {
			throw error(500, 'No book id');
		}

		await graphql(`
			mutation DeleteBook($input: DeleteInput!) {
				delete(input: $input) {
					id
				}
			}
		`).mutate({ input: { id } }, { event });

		throw redirect(303, '/');
	}
};
