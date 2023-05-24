import { expect } from '@playwright/test';
import { test } from '../fixtures.js';

test('can add to basket', async ({ page }) => {
	// given
	const main = page.getByRole('main');
	const nav = page.getByRole('navigation');
	await page.goto('/');

	const book = main
		.getByRole('region', {
			name: 'Bestsellery'
		})
		.getByRole('article', {
			name: 'Kicia Kocia. Wiosna'
		});

	await expect.soft(book.getByText('6,45 zł')).toBeVisible();

	await book.getByRole('link').click();
	await expect.soft(page).toHaveTitle('Kicia Kocia. Wiosna');

	await expect.soft(main.getByText('6,45 zł')).toBeVisible();

	// when
	await main.getByRole('button', { name: 'To cart', exact: true }).click();
	// then
	await nav.getByRole('button', { name: 'show cart total' }).click();
	await nav.getByRole('link', { name: 'to checkout' }).click();
	await expect.soft(page).toHaveTitle('Basket');
	await expect.soft(main.getByText('Total 6,45 zł')).toBeVisible();
	await expect
		.soft(
			main.getByRole('heading', {
				name: 'Kicia Kocia. Wiosna'
			})
		)
		.toBeVisible();
});

test('can quickly add to basket', async ({ page }) => {
	// given
	const main = page.getByRole('main');
	const nav = page.getByRole('navigation');
	await page.goto('/');

	const book = main
		.getByRole('region', {
			name: 'Bestsellery'
		})
		.getByRole('article', {
			name: 'Kicia Kocia. Wiosna'
		});

	await expect.soft(book.getByText('6,45 zł')).toBeVisible();

	// when
	await book.getByRole('button', { name: 'Add to cart' }).click();
	// then

	await nav.getByRole('button', { name: 'show cart total' }).click();
	await nav.getByRole('link', { name: 'to checkout' }).click();
	await expect.soft(page).toHaveTitle('Basket');
	await expect.soft(main.getByText('Total 6,45 zł')).toBeVisible();
	await expect
		.soft(
			main.getByRole('heading', {
				name: 'Kicia Kocia. Wiosna'
			})
		)
		.toBeVisible();
});

test('keeps contents after register', async ({ page, register }) => {
	// given
	const main = page.getByRole('main');
	const nav = page.getByRole('navigation');
	await page.goto('/');

	const book = main
		.getByRole('region', {
			name: 'Bestsellery'
		})
		.getByRole('article', {
			name: 'Kicia Kocia. Wiosna'
		});

	await expect.soft(book.getByText('6,45 zł')).toBeVisible();
	await book.getByRole('button', { name: 'Add to cart' }).click();
	// when
	await register();
	// then
	await expect.soft(nav.getByRole('link', { name: 'register' })).not.toBeVisible();
	await expect.soft(nav.getByRole('link', { name: 'login' })).not.toBeVisible();

	await nav.getByRole('button', { name: 'show cart total' }).click();
	await nav.getByRole('link', { name: 'to checkout' }).click();
	await expect.soft(page).toHaveTitle('Basket');
	await expect.soft(main.getByText('Total 6,45 zł')).toBeVisible();
	await expect
		.soft(
			main.getByRole('heading', {
				name: 'Kicia Kocia. Wiosna'
			})
		)
		.toBeVisible();
});

test('clears after logout', async ({ page, register }) => {
	// given
	const main = page.getByRole('main');
	const nav = page.getByRole('navigation');
	await page.goto('/');
	await register();

	const book = main
		.getByRole('region', {
			name: 'Bestsellery'
		})
		.getByRole('article', {
			name: 'Kicia Kocia. Wiosna'
		});

	await expect.soft(book.getByText('6,45 zł')).toBeVisible();
	await book.getByRole('button', { name: 'Add to cart' }).click();
	// when
	await nav.getByRole('button', { name: 'show menu' }).click();
	await nav.getByRole('button', { name: 'logout' }).click();
	// then
	await expect.soft(nav.getByRole('link', { name: 'register' })).toBeVisible();
	await expect.soft(nav.getByRole('link', { name: 'login' })).toBeVisible();

	await nav.getByRole('button', { name: 'show cart total' }).click();
	await nav.getByRole('link', { name: 'to checkout' }).click();
	await expect.soft(page).toHaveTitle('Basket');
	await expect.soft(main.getByText('Total 0,00 zł')).toBeVisible();
	await expect
		.soft(
			main.getByRole('heading', {
				name: 'Kicia Kocia. Wiosna'
			})
		)
		.not.toBeVisible();
});

test('keeps between logins', async ({ page, register, login }) => {
	// given
	const main = page.getByRole('main');
	const nav = page.getByRole('navigation');
	await page.goto('/');
	const user = await register();

	const book = main
		.getByRole('region', {
			name: 'Bestsellery'
		})
		.getByRole('article', {
			name: 'Kicia Kocia. Wiosna'
		});

	await expect.soft(book.getByText('6,45 zł')).toBeVisible();
	await book.getByRole('button', { name: 'Add to cart' }).click();
	await nav.getByRole('button', { name: 'show menu' }).click();
	await nav.getByRole('button', { name: 'logout' }).click();
	await expect.soft(nav.getByRole('link', { name: 'register' })).toBeVisible();
	await expect.soft(nav.getByRole('link', { name: 'login' })).toBeVisible();

	await nav.getByRole('button', { name: 'show cart total' }).click();
	await nav.getByRole('link', { name: 'to checkout' }).click();
	await expect.soft(page).toHaveTitle('Basket');
	await expect.soft(main.getByText('Total 0,00 zł')).toBeVisible();
	await expect
		.soft(
			main.getByRole('heading', {
				name: 'Kicia Kocia. Wiosna'
			})
		)
		.not.toBeVisible();
	// when
	await login(user);
	// then
	await expect.soft(nav.getByRole('link', { name: 'register' })).not.toBeVisible();
	await expect.soft(nav.getByRole('link', { name: 'login' })).not.toBeVisible();

	await nav.getByRole('button', { name: 'show cart total' }).click();
	await nav.getByRole('link', { name: 'to checkout' }).click();
	await expect.soft(page).toHaveTitle('Basket');
	await expect.soft(main.getByText('Total 6,45 zł')).toBeVisible();
	await expect
		.soft(
			main.getByRole('heading', {
				name: 'Kicia Kocia. Wiosna'
			})
		)
		.toBeVisible();
});
