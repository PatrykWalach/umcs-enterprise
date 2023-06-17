import { graphql } from '$houdini';
import BasketBook from '$lib/BasketBook.server';
import UnbasketBook from '$lib/UnbasketBook.server';
import { BASKET_COOKIE } from '$lib/constants';
import { setBasket } from '$lib/setBasket';
import { error, redirect } from '@sveltejs/kit';
import type { Actions } from './$houdini';

const MakePurchase = graphql(/* GraphQL */ `
	mutation MakePurchase($input: MakePurchaseInput!) {
		makePurchase(input: $input) {
			purchase {
				id
			}
		}
	}
`);

export const actions: Actions = {
	basket_book: async (event) => {
		const { id } = Object.fromEntries(await event.request.formData());
		if (typeof id !== 'string') {
			throw error(500, 'No book id');
		}

		await BasketBook({ input: { book: { id } } }, event);

		throw redirect(303, '/basket');;
	},
	unbasket_book: async (event) => {
		const { id } = Object.fromEntries(await event.request.formData());
		if (typeof id !== 'string') {
			throw new Error('No book id');
		}

		await UnbasketBook({ input: { book: { id } } }, event);

		throw redirect(303, '/basket');;
	},
	reset_basket: async (event) => {
		setBasket(event, undefined);

		throw redirect(303, '/basket');
	},
	make_purchase: async (event) => {
	

		const response = await MakePurchase.mutate(
			{ input: { basket: { id: event.cookies.get(BASKET_COOKIE) } } },
			{ event }
		);

		if (response.data?.makePurchase?.purchase?.id) {
			setBasket(event, undefined);
			throw redirect(303, '/purchase/' + response.data.makePurchase.purchase.id);
		}

		throw error(500, {
			message: response.errors?.map((error) => error.message).join() ?? 'Internal error'
		});
	}
};
