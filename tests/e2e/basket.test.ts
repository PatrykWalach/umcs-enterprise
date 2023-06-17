import { expect } from '@playwright/test';
import { test } from '../fixtures.js';
import HomePage from './HomePage.js';

test('keeps contents after register', async ({ page, register }) => {
	// given

	await page.goto('/');

	const homepage = new HomePage(page);
	const book = homepage.bestsellers.book('Kicia Kocia. Wiosna');
	await expect.soft(book.price).toHaveText('6,45 zł');
	const bookpage = await book.navigate();

	await bookpage.addToBasket();
	// when
	await register();
	// then
	await expect.soft(homepage.nav.register).not.toBeVisible();
	await expect.soft(homepage.nav.login).not.toBeVisible();
	const basketpage = await homepage.nav.goToBasket();

	await expect.soft(basketpage.main.getByText('Total 6,45 zł')).toBeVisible();
	await expect.soft(basketpage.book('Kicia Kocia. Wiosna')).toBeVisible();
});

test('keeps after logout', async ({ page, register }) => {
	// given
	await page.goto('/');
	await register();

	const homepage = new HomePage(page);
	const book = homepage.bestsellers.book('Kicia Kocia. Wiosna');
	await expect.soft(book.price).toHaveText('6,45 zł');
	const bookpage = await book.navigate();

	await bookpage.addToBasket();
	// when
	await bookpage.nav.logout();
	// then
	const basketpage = await homepage.nav.goToBasket();

	await expect.soft(basketpage.main.getByText('Total 6,45 zł')).toBeVisible();
	await expect.soft(basketpage.book('Kicia Kocia. Wiosna')).toBeVisible();
});
