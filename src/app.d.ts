// See https://kit.svelte.dev/docs/types#app

import type { GraphQLClient } from 'graphql-request';

// for information about these interfaces
declare global {
	namespace App {
		// interface Error {}
		interface Locals {
			client: GraphQLClient;
			formData<T extends ParsedQuery<string | boolean | number>>(): Promise<T>;
		}
		// interface PageData {}
		// interface Platform {}
	}
}

export { };

