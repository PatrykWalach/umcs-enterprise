import { graphql } from '$houdini';
import { redirect, type Actions } from '@sveltejs/kit';

const SendPurchase = graphql(/* GraphQL */ `
	mutation SendPurchase($input: SendPurchaseInput!) {
		sendPurchase(input: $input) {
			__typename
		}
	}
`);

export const actions: Actions = {
	default: async (event) => {
		await SendPurchase.mutate({ input: { iD: String(event.params.id) } }, { event });

		throw redirect(303, '/purchase/' + event.params.id);
	}
};
