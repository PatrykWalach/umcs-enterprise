// See https://kit.svelte.dev/docs/types#app

import type { api } from "$lib/client.server";
import type { Client } from "ketting";



// for information about these interfaces
declare global {
	namespace App {
		// interface Error {}
		interface Locals {
			client: Client;
		}
		// interface Session {}
		interface PageData {
			flash?: { type: 'BASKET_BOOK' };
		}

		// interface Platform {}
	}
}

export { };

