// See https://kit.svelte.dev/docs/types#app

import type { Client } from '@urql/core';

// for information about these interfaces
declare global {
	namespace App {
		// interface Error {}
		interface Locals {
			client: Client;
		}
		// interface PageData {}
		// interface Platform {}
	}
}

export { };

