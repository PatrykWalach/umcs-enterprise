import { test as base, expect } from '@playwright/test';
import * as crypto from 'crypto';

interface Viewer {
	name: string;
	password: string;
}

export const test = base.extend<{
	register: (options?: Partial<Viewer>) => Promise<Viewer>;
	login: (options: Viewer) => Promise<Viewer>;
	admin: Viewer;
}>({
	async admin({ page, login }, use) {
		await page.goto('/');
		await use(await login({ name: 'admin', password: 'admin' }));
	},
	async register({ page, login }, use) {
		const users: Viewer[] = [];

		await use(async ({ name = crypto.randomUUID(), password = crypto.randomUUID() } = {}) => {
			const main = page.getByRole('main');
			const nav = page.getByRole('navigation');
			await nav.getByRole('link', { name: 'register' }).click();
			await main.getByLabel('Username').fill(name);
			await main.getByLabel('Password').fill(password);
			await main.getByRole('button', { name: 'register' }).click();
			await expect.soft(page).toHaveTitle('Home');

			users.push({ name, password });
			await login({ name, password });
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

			// await page.getByRole('checkbox', { name: 'read' }).check()
			// await page.getByRole('checkbox', { name: 'profile' }).check()
			// await page.getByRole('checkbox', { name: 'write' }).check()
			// await page.getByRole('button', { name: 'Submit Consent' }).click();

			return { name, password };
		});
	}
});
