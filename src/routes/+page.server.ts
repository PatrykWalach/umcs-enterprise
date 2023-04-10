import { graphql } from '$gql';
import BasketBook from '$lib/BasketBook.server';
import type { ServerLoad } from '@sveltejs/kit';
import type { Actions } from './$types';

export const load: ServerLoad = async ({ locals }) => {
	const { data ,error} = await locals.client.query(
		graphql(/* GraphQL */ `
			query HomeQuery {
				new: books(first: 12, orderBy: { releasedAt: DESC }) {
					edges {
						node {
							...Book_book
							id
						}
					}
				}
				popular: books(first: 12, orderBy: { popularity: DESC }) {
					edges {
						node {
							...Book_book
							id
						}
					}
				}
			}
		`),
		{}
	);

	if (!data) {
		throw error;
	}

	return {
		HomeQuery: data
	};
};

export const actions: Actions = {
	default: async ({ locals, request, cookies }) => {
		const { id } = Object.fromEntries(await request.formData());

		if (typeof id !== 'string') {
			throw new Error('No book id');
		}

		await BasketBook(
			{
				locals,
				cookies
			},
			{ id }
		);

		return {};
	}
};
