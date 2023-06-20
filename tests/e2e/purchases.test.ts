import { expect } from '@playwright/test';
import { test } from '../fixtures.js';
import HomePage from './HomePage.js';

test('can see purchases', async ({ page, register }) => {
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
	// when
  const purchasespage = await purchasepage.nav.purchases()
	// then
	await expect.soft(purchasespage.main.getByText('MADE')).toBeVisible();
	await expect.soft(purchasespage.main.getByText('6,45 zł')).toBeVisible();
});
