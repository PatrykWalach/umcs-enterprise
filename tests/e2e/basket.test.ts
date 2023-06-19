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

	await expect.soft(basketpage.total).toHaveText('6,45 zł');
	await expect.soft(basketpage.book('Kicia Kocia. Wiosna').loc).toBeVisible();
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

	await expect.soft(basketpage.total).toHaveText('6,45 zł');
	await expect.soft(basketpage.book('Kicia Kocia. Wiosna').loc).toBeVisible();
});

test('can remove on second page @slow', async ({ page, register }) => {
	// given
	await page.goto('/');
	await register();

	const homepage = new HomePage(page);

	for (const title of [
		'Kicia Kocia. Wiosna',
		'Wołodymyr Zełenski. Ukraina we krwi',
		'Przesłanie z Ukrainy',
		'Wioska wdów. Szokująca historia morderczyń z wioski Nagyrév',
		'Lekarz kwantowy',
		'Dom na kurzych łapach',
		'Sława zniesławia. Rozmowa rzeka',
		'Córka z Włoch. Utracone córki. Tom 1',
		'Przędza. W poszukiwaniu wewnętrznej wolności'
	]) {
		const book = homepage.bestsellers.book(title);
		const bookpage = await book.navigate();
		await bookpage.addToBasket();
		await page.goto('/');
		await expect.soft(page).toHaveTitle('Home');
	}

	const basketpage = await homepage.nav.goToBasket();

	await expect.soft(basketpage.total).toHaveText('244,24 zł');
	await basketpage.next();
	const book = basketpage.book('Lekarz kwantowy');

	await expect.soft(book.loc).toBeVisible();
	await book.addMore();
	await expect.soft(basketpage.total).toHaveText('272,64 zł');
	await book.remove();
	await expect.soft(basketpage.total).toHaveText('244,24 zł');
	// when
	await book.remove();
	// then
	await expect.soft(basketpage.total).toHaveText('215,84 zł');
	await expect.soft(basketpage.book('Kicia Kocia. Wiosna').loc).toBeVisible();
});
