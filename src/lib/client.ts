import { z, type TypeOf } from 'zod';

export const bookListItem = z.object({
	title: z.string(),
	authors: z.array(z.string()),
	covers: z.record(z.string()),
	price: z.number(),
	id: z.string()
});

export type BookListItem = TypeOf<typeof bookListItem>;

export const page = z.object({
	page: z.object({
		size: z.number(),
		totalElements: z.number(),
		totalPages: z.number(),
		number: z.number()
	})
});

export type Page = TypeOf<typeof page>;
