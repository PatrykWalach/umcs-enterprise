import { test as base } from '@playwright/test';

import { preview, type PreviewServer } from 'vite';

interface Viewer {
	name: string;
	password: string;
}

export const test = base.extend<
	{
		admin: Viewer;
		login(username: string, password: string): Promise<void>;
	},
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
	},
	async login({ page }, use) {
		await use(async (username: string, password: string) => {
			await page.goto('/login');
			await page.getByLabel('Username', ).fill(username);
			await page.getByLabel('Password', ).fill(password);
			await page.getByRole('button', { name: 'login' }).click();
		});
	},
	async admin({ login }, use) {
		await login('admin', 'admin');
		await use({ name: 'admin', password: 'admin' });
	}
});
