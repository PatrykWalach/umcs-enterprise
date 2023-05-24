import BasketBook from '$lib/BasketBook.server';
import { error } from '@sveltejs/kit';
import type { Actions } from './$types';

import * as Api from '$lib/client';
import { bookListItem } from '$lib/client';
import { Effect, pipe } from 'effect';

import * as Ketting from '$lib/Ketting.server';

export const load = async (event) => {
	const params = event.url.searchParams;
	const href = params.get('href');
	const sort = `${params.get('by') ?? 'releaseDate'},${params.get('order') ?? 'desc'}`;

	const books = href
		? Ketting.go(href)
		: pipe(
				Ketting.go(),
				Ketting.follow('books', {
					projection: 'list-item',
					sort
				})
		  );

	return {
		BooksQuery: pipe(
			books,
			Effect.flatMap((books) =>
				Effect.structPar({
					page: Effect.structPar({
						info: pipe(
							Effect.succeed(books),
							Effect.flatMap(Ketting.get()),
							Effect.map(({ data }) => data),
							Effect.mapTryCatch(Api.page.parse, Ketting.ParseError.new),
							Effect.map(({ page }) => page)
						),
						last: pipe(Effect.succeed(books), Ketting.uri('last')),
						next: pipe(Effect.succeed(books), Ketting.uri('next')),
						prev: pipe(Effect.succeed(books), Ketting.uri('prev')),
						first: pipe(Effect.succeed(books), Ketting.uri('first'))
					}),
					books: pipe(
						Effect.succeed(books),
						Ketting.followAll('books'),
						Ketting.parseAll(bookListItem)
					)
				})
			),
			Ketting.retry(),
			Effect.orDie,
			Effect.provideService(Ketting.Client)(event.locals.client),
			Effect.unsafeRunPromise
		)
	};
};

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
