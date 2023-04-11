import { test as base } from '@playwright/test';

import { preview, type PreviewServer } from 'vite';

export const test = base.extend<
	Record<never, never>,
	{
		app: PreviewServer;
	}
>({
	app: [
		// eslint-disable-next-line no-empty-pattern
		async ({}, use) => {
			const server = await preview({ logLevel: 'silent' });

			await use(server);
		},
		{ scope: 'worker' }
	],

	async baseURL({ app }, use) {
		const [address] = app.resolvedUrls.local ?? [];
		await use(address);
	}
});
