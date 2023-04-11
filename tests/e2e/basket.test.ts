import { expect } from '@playwright/test';
import { test } from '../fixtures.js';

test('can add to basket', async ({ page }) => {
	// given
	await page.goto('/');
 

	const book = page.getByRole('main')
		.getByRole('region', {
			name: 'Bestsellers'
		})
		.getByRole('article', {
			name: 'Kicia Kocia. Wiosna'
		});

	await expect.soft(book.getByText('6,45 zł')).toBeVisible();

	await book.getByRole('link').click();
 
	await expect.soft(page.getByRole('main').getByText('6,45 zł')).toBeVisible();

	// when
	await page.getByRole('main').getByRole('button', { name: 'To cart', exact: true }).click();
	// then
	await page.getByRole('navigation').getByRole('button', { name: 'Show cart total' }).click();
	await expect.soft(page.getByRole('navigation').getByText('Total: 6,45 zł')).toBeVisible();
	await page.getByRole('navigation').getByRole('link', { name: 'To checkout' }).click();
	await expect(
		page.getByRole('main').getByRole('heading', {
			name: 'Kicia Kocia. Wiosna'
		})
	).toBeVisible();
});

test('can quickly add to basket', async ({ page }) => {
	// given
	await page.goto('/');
 

	const book = page.getByRole('main')
		.getByRole('region', {
			name: 'Bestsellers'
		})
		.getByRole('article', {
			name: 'Kicia Kocia. Wiosna'
		});

	await expect.soft(book.getByText('6,45 zł')).toBeVisible();

	// when
	await book.getByRole('button', { name: 'Add to cart' }).click();
	// then

	await page.getByRole('navigation').getByRole('button', { name: 'Show cart total' }).click();
	await expect.soft(page.getByRole('navigation').getByText('Total: 6,45 zł')).toBeVisible();
	await page.getByRole('navigation').getByRole('link', { name: 'To checkout' }).click();
 
	await expect(
		page.getByRole('main').getByRole('heading', {
			name: 'Kicia Kocia. Wiosna'
		})
	).toBeVisible();
});
