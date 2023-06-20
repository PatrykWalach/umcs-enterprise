import { expect } from '@playwright/test';
import { test } from '../fixtures.js';
import HomePage from './HomePage.js';

test('can make purchase', async ({ page, register }) => {
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
	await expect.soft(purchasepage.total).toHaveText('6,45 zł');
});

test('can pay purchase', async ({ page, register }) => {
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

	const purchasepage = await basketpage.makePurchase();
	await expect.soft(purchasepage.send).not.toBeVisible();
	// when
	const paypalpage = await purchasepage.pay();
	// then
	await paypalpage.paymentCard();
	await expect.soft(purchasepage.send).not.toBeVisible();
	await expect.soft(purchasepage.total).toHaveText('6,45 zł');
});

test('can send purchase', async ({ page, admin }) => {
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
	await expect.soft(purchasepage.total).toHaveText('6,45 zł');
	await expect.soft(purchasepage.send).not.toBeVisible();

	const paypalpage = await purchasepage.pay();
	await paypalpage.paymentCard();
	await expect.soft(purchasepage.total).toHaveText('6,45 zł');
	// when
	await purchasepage.send.click();
	// then
	await expect.soft(purchasepage.status).toHaveText('SENT');
});
