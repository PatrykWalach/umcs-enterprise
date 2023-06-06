import { expect } from '@playwright/test';
import { test } from '../fixtures.js';
import HomePage, { BookPage, CartPage } from './HomePage.js';

test('can add to basket twice', async ({ page }) => {
	// given

	await page.goto('/');

	const homepage = new HomePage(page);
	const book = homepage.bestsellers.book('Kicia Kocia. Wiosna');
	await expect.soft(book.loc.getByText('6,45 zł')).toBeVisible();
	await book.addToCart();

	const bookpage = new BookPage(page);
	await expect.soft(page).toHaveTitle('Kicia Kocia. Wiosna');
	await expect.soft(bookpage.nav.cartItems.getByText('1')).toBeVisible();
	// when
	await bookpage.addToCart();
	// then
	await expect.soft(bookpage.nav.cartItems.getByText('2')).toBeVisible();
	await bookpage.nav.goToCart();

	const cartpage = new CartPage(page);
	await expect.soft(cartpage.main.getByText('Total 12,90 zł')).toBeVisible();
	await expect.soft(cartpage.book('Kicia Kocia. Wiosna')).toBeVisible();
});

test('can quickly add to basket', async ({ page }) => {
	// given

	await page.goto('/');

	const homepage = new HomePage(page);
	const book = homepage.bestsellers.book('Kicia Kocia. Wiosna');
	await expect.soft(book.loc.getByText('6,45 zł')).toBeVisible();
	// when
	await book.addToCart();
	// then

	const bookpage = new BookPage(page);
	await expect.soft(page).toHaveTitle('Kicia Kocia. Wiosna');
	await expect.soft(bookpage.nav.cartItems.getByText('1')).toBeVisible();
	await bookpage.nav.goToCart();

	const cartpage = new CartPage(page);
	await expect.soft(cartpage.main.getByText('Total 6,45 zł')).toBeVisible();
	await expect.soft(cartpage.book('Kicia Kocia. Wiosna')).toBeVisible();
});

test('keeps contents after register', async ({ page, register }) => {
	// given

	await page.goto('/');

	const homepage = new HomePage(page);
	const book = homepage.bestsellers.book('Kicia Kocia. Wiosna');
	await expect.soft(book.loc.getByText('6,45 zł')).toBeVisible();
	await book.addToCart();
	// when
	await register();
	// then
	await expect.soft(homepage.nav.register).not.toBeVisible();
	await expect.soft(homepage.nav.login).not.toBeVisible();
	await homepage.nav.goToCart();

	const cartpage = new CartPage(page);
	await expect.soft(cartpage.main.getByText('Total 6,45 zł')).toBeVisible();
	await expect.soft(cartpage.book('Kicia Kocia. Wiosna')).toBeVisible();
});

test('clears after logout', async ({ page, register }) => {
	// given
	await page.goto('/');
	await register();

	const homepage = new HomePage(page);
	const book = homepage.bestsellers.book('Kicia Kocia. Wiosna');
	await expect.soft(book.loc.getByText('6,45 zł')).toBeVisible();
	await book.addToCart();
	// when
	await homepage.nav.logout();
	// then

	await homepage.nav.goToCart();
	const cartpage = new CartPage(page);
	await expect.soft(cartpage.main.getByText('Total 0,00 zł')).toBeVisible();
	await expect.soft(cartpage.book('Kicia Kocia. Wiosna')).not.toBeVisible();
});

test('keeps between logins', async ({ page, register, login }) => {
	// given

	await page.goto('/');
	const user = await register();

	const homepage = new HomePage(page);
	const book = homepage.bestsellers.book('Kicia Kocia. Wiosna');
	await expect.soft(book.loc.getByText('6,45 zł')).toBeVisible();
	await book.addToCart();
	await homepage.nav.logout();
	await homepage.nav.goToCart();

	const cartpage = new CartPage(page);
	await expect.soft(cartpage.main.getByText('Total 0,00 zł')).toBeVisible();
	await expect.soft(cartpage.book('Kicia Kocia. Wiosna')).not.toBeVisible();
	// when
	await login(user);
	// then
	await expect.soft(homepage.nav.register).not.toBeVisible();
	await expect.soft(homepage.nav.login).not.toBeVisible();
	await homepage.nav.goToCart();
	
	await expect.soft(cartpage.main.getByText('Total 6,45 zł')).toBeVisible();
	await expect.soft(cartpage.book('Kicia Kocia. Wiosna')).toBeVisible();
});
