import BasketBook from '$lib/BasketBook.server';
import { error } from '@sveltejs/kit';
import type { Actions, PageServerLoad } from './$types';

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

import * as Ketting from '$lib/Ketting.server';
import { bookListItem } from '$lib/client';
import { Effect, pipe } from 'effect';
import type { LinkVariables } from 'ketting/dist/link';

function getBooks(variables?: LinkVariables) {
	return pipe(
		Ketting.go(),
		Ketting.follow('books', {
			projection: 'list-item',
			size: 12,
			...variables
		}),
		Ketting.followAll('books'),
		Ketting.parseAll(bookListItem)
	);
}

export const load: PageServerLoad = async (event) => {
	return {
		HomeQuery: pipe(
			Effect.structPar({
				popular: getBooks({
					sort: 'releaseDate,desc'
				}),
				new: getBooks({
					sort: 'popularity,desc'
				})
			}),
			Ketting.retry(),
			Effect.orDie,
			Effect.provideService(Ketting.Client)(event.locals.client),
			Effect.unsafeRunPromise
		)
	};
};
