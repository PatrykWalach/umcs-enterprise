import { redirect, type Handle } from '@sveltejs/kit';

import { Client, fetchExchange } from '@urql/core';
import { authExchange } from '@urql/exchange-auth';

export const handle: Handle = ({ event, resolve }) => {
	const token = event.cookies.get('enterprise-token');

	const client = new Client({
		url: 'http://localhost:8080/graphql',
		fetch: event.fetch,
		fetchOptions: {
			signal: event.request.signal
		},
		exchanges: [
			authExchange(async (utils) => {
				return {
					addAuthToOperation(operation) {
						if (!token) return operation;
						return utils.appendHeaders(operation, {
							Authorization: `Bearer ${token}`
						});
					},
					didAuthError(error, _operation) {
						return error.graphQLErrors.some((e) => e.extensions?.errorType === 'UNAUTHENTICATED');
					},
					async refreshAuth() {
						event.cookies.set('enterprise-token', '');
						throw redirect(303, '/login');
					}
				};
			}),
			fetchExchange
		]
	});
	event.locals.client = {
		async request(query, variables) {
			const { data, error } = await client.query(query, variables);

			if (!data) {
				throw error;
			}
			return data;
		},
		async mutation(query, variables) {
			const { data, error } = await client.mutation(query, variables);

			if (!data) {
				throw error;
			}
			
			return data;
		},
	};
	return resolve(event);
};
