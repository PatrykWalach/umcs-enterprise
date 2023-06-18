import { expect } from '@playwright/test';
import { test } from '../fixtures.js';
import HomePage, { BasketPage, BookPage } from './HomePage.js';

test('can add to basket', async ({ page }) => {
	// given

	await page.goto('/');

	const homepage = new HomePage(page);
	const book = homepage.bestsellers.book('Kicia Kocia. Wiosna');
	await expect.soft(book.price).toHaveText('6,45 zł');
	await book.navigate();

	const bookpage = new BookPage(page);
	// when
	await bookpage.addToBasket();
	// then

	await bookpage.nav.goToBasket();

	const basketpage = new BasketPage(page);
	await expect.soft(basketpage.main.getByText('Total 6,45 zł')).toBeVisible();
	await expect.soft(basketpage.book('Kicia Kocia. Wiosna').loc).toBeVisible();
});

test('can add to basket twice', async ({ page }) => {
	// given

	await page.goto('/');

	const homepage = new HomePage(page);
	const book = homepage.bestsellers.book('Kicia Kocia. Wiosna');
	await expect.soft(book.price).toHaveText('6,45 zł');
	const bookpage = await book.navigate();

	// when
	await bookpage.addToBasket();
	await bookpage.addToBasket();
	// then
	const basketpage = await bookpage.nav.goToBasket();

	await expect.soft(basketpage.main.getByText('Total 12,90 zł')).toBeVisible();
	await expect.soft(basketpage.book('Kicia Kocia. Wiosna').loc).toBeVisible();
});
