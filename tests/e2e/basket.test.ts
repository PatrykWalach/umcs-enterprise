import { expect } from '@playwright/test';
import { test } from '../fixtures.js';

test('can add to basket', async ({ page }) => {
	// given
	const main = page.getByRole('main');
	const nav = page.getByRole('navigation');
	await page.goto('/');

	const book = main
		.getByRole('region', {
			name: 'Bestsellers'
		})
		.getByRole('article', {
			name: 'Kicia Kocia. Wiosna'
		});

	await expect(book.getByText('6,45 zł')).toBeVisible();

	await book.getByRole('link').click();
	await expect(page).toHaveTitle('Kicia Kocia. Wiosna')

	await expect(main.getByText('6,45 zł')).toBeVisible();

	// when
	await main.getByRole('button', { name: 'To cart', exact: true }).click();
	// then
	await nav.getByRole('button', { name: 'Show cart total' }).click();
	await expect(nav.getByText('Total: 6,45 zł')).toBeVisible();
	await nav.getByRole('link', { name: 'To checkout' }).click();
	await expect(page).toHaveTitle('Basket')
	await expect(
		main.getByRole('heading', {
			name: 'Kicia Kocia. Wiosna'
		})
	).toBeVisible();
});

test('can quickly add to basket', async ({ page }) => {
	// given
	const main = page.getByRole('main');
	const nav = page.getByRole('navigation');
	await page.goto('/');

	const book = main
		.getByRole('region', {
			name: 'Bestsellers'
		})
		.getByRole('article', {
			name: 'Kicia Kocia. Wiosna'
		});

	await expect(book.getByText('6,45 zł')).toBeVisible();

	// when
	await book.getByRole('button', { name: 'Add to cart' }).click();
	// then
	
	await nav.getByRole('button', { name: 'Show cart total' }).click();
	await expect(nav.getByText('Total: 6,45 zł')).toBeVisible();
	await nav.getByRole('link', { name: 'To checkout' }).click();
	await expect(page).toHaveTitle('Basket')
	await expect(
		main.getByRole('heading', {
			name: 'Kicia Kocia. Wiosna'
		})
	).toBeVisible();
});
