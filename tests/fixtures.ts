import { test as base } from '@playwright/test';

import { preview, type PreviewServer } from 'vite';

interface Viewer {
	name: string;
	password: string;
}

export const test = base.extend<
	{
		register: (options?: Partial<Viewer>) => Promise<Viewer>;
		login: (options: Viewer) => Promise<Viewer>;
		admin: Viewer;
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
	async admin({ page, login }, use) {
		await page.goto('/');
		await use(await login({ name: 'admin', password: 'admin' }));
	},
	async register(
		{
			page
			// , login
		},
		use
	) {
		const users: Viewer[] = [];

		await use(async ({ name = crypto.randomUUID(), password = crypto.randomUUID() } = {}) => {
			const main = page.getByRole('main');
			const nav = page.getByRole('navigation');
			await nav.getByRole('link', { name: 'register' }).click();
			await main.getByLabel('Username').fill(name);
			await main.getByLabel('Password').fill(password);
			await main.getByRole('button', { name: 'register' }).click();
			users.push({ name, password });
			return { name, password };
		});

		/**
		 * @TODO delete account
		 * for (const user of users) {
		 * 	 await login(user);
		 *   await nav.getByRole('link', { name: 'profile' }).click();
		 *   await main.getByRole('button', { name: 'delete' }).click();
		 * }
		 * */
	},
	async login({ page }, use) {
		await use(async ({ name, password }) => {
			const main = page.getByRole('main');
			const nav = page.getByRole('navigation');
			await nav.getByRole('link', { name: 'login' }).click();
			await main.getByLabel('Username').fill(name);
			await main.getByLabel('Password').fill(password);
			await main.getByRole('button', { name: 'login' }).click();

			return { name, password };
		});
	}
});
