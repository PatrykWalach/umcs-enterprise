// See https://kit.svelte.dev/docs/types#app

// for information about these interfaces
declare global {
	namespace App {
		// interface Error {}
		// interface Locals {}
		interface Session {
			token: string | undefined;
			basket: string | undefined;
		}
		// interface PageData {}
		// interface Platform {}
	}
}

export {};
