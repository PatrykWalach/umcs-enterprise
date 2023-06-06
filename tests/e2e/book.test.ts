import { expect } from '@playwright/test';
import { test } from '../fixtures.js';
import HomePage, { BookPage, CartPage } from './HomePage.js';

test('can add to basket', async ({ page }) => {
	// given

	await page.goto('/');

	const homepage = new HomePage(page);
	const book = homepage.bestsellers.book('Kicia Kocia. Wiosna');
	await expect.soft(book.loc.getByText('6,45 zł')).toBeVisible();
	await book.loc.getByRole('link').click();

	const bookpage = new BookPage(page);
	await expect.soft(page).toHaveTitle('Kicia Kocia. Wiosna');
	// when
	await bookpage.addToCart();
	// then
	await expect.soft(bookpage.nav.cartItems.getByText('1')).toBeVisible();
	await bookpage.nav.goToCart();

	const cartpage = new CartPage(page);
	await expect.soft(cartpage.main.getByText('Total 6,45 zł')).toBeVisible();
	await expect.soft(cartpage.book('Kicia Kocia. Wiosna')).toBeVisible();
});
