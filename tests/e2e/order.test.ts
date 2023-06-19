import { expect } from '@playwright/test';
import { test } from '../fixtures.js';
import HomePage from './HomePage.js';

test('can make order', async ({ page, register }) => {
	// given

	await page.goto('/');
	await register();

	const homepage = new HomePage(page);
	const book = homepage.bestsellers.book('Kicia Kocia. Wiosna');
	await expect.soft(book.price).toHaveText('6,45 zł');

	const bookpage = await book.navigate();
	await bookpage.addToBasket();

	const basketpage = await bookpage.nav.goToBasket();
	await expect.soft(basketpage.total).toHaveText('6,45 zł');
	await expect.soft(basketpage.book('Kicia Kocia. Wiosna').loc).toBeVisible();

	// when
	const purchasepage = await basketpage.makePurchase();
	// then
	await expect.soft(purchasepage.send).not.toBeVisible();
	await expect.soft(purchasepage.status).toHaveText('MADE');
	await expect.soft(purchasepage.total).toHaveText('6,45 zł');
});

test('can send order', async ({ page, admin }) => {
	// given

	const homepage = new HomePage(page);
	const book = homepage.bestsellers.book('Kicia Kocia. Wiosna');
	await expect.soft(book.price).toHaveText('6,45 zł');

	const bookpage = await book.navigate();
	await bookpage.addToBasket();

	const basketpage = await bookpage.nav.goToBasket();
	await expect.soft(basketpage.total).toHaveText('6,45 zł');
	await expect.soft(basketpage.book('Kicia Kocia. Wiosna').loc).toBeVisible();

	const purchasepage = await basketpage.makePurchase();
	await expect.soft(purchasepage.status).toHaveText('MADE');
	await expect.soft(purchasepage.total).toHaveText('6,45 zł');
	// when
	purchasepage.send.click();
	// then
	await expect.soft(purchasepage.status).toHaveText('SENT');
});
