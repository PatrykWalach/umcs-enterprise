import { expect } from '@playwright/test';
import { test } from '../fixtures.js';

test('can upload cover', async ({ page, admin }) => {
	// given
	const main = page.getByRole('main');
	const nav = page.getByRole('navigation');
	await page.goto('/admin/add');

	const form = main.getByRole('form');

	await form.getByLabel('Title').fill('Foo');
	await form.getByLabel('Author').fill('Bar');
	await form.getByLabel('Price').type('12.5');
	await form.getByLabel('Release date').fill('2023-04-24');
	await form.getByLabel('Cover').setInputFiles('src/test/resources/cover.jpg');

	// when
	await form.getByRole('button', { name: 'Submit' }).click();
	// then
	await expect.soft(page).toHaveTitle('Foo');

	await expect.soft(main.getByRole('heading', { name: 'Foo' })).toBeVisible();
	await expect.soft(main.getByText('Bar')).toBeVisible();
	await expect.soft(main.getByText('12,50 zł')).toBeVisible();
	await main
		.getByRole('button', {
			name: 'Delete'
		})
		.click();
	await expect.soft(page).toHaveTitle('Home');
});
