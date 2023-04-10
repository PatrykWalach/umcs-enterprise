// See https://kit.svelte.dev/docs/types#app

import type { AnyVariables, DocumentInput, OperationContext } from "@urql/core";


// for information about these interfaces
declare global {
	namespace App {
		// interface Error {}
		interface Locals {
			client: {
				request<Data = any, Variables extends AnyVariables = AnyVariables>(
					query: DocumentInput<Data, Variables>,
					variables: Variables,
					context?: Partial<OperationContext>
				): PromiseLike<Data>;
				mutation<Data = any, Variables extends AnyVariables = AnyVariables>(
					query: DocumentInput<Data, Variables>,
					variables: Variables,
					context?: Partial<OperationContext>
				): PromiseLike<Data>;
			};
		}
		// interface PageData {}
		// interface Platform {}
	}
}

export { };

