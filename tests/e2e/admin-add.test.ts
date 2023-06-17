import { expect } from '@playwright/test';
import { test } from '../fixtures.js';
import HomePage, { AddBookPage, BookPage } from './HomePage.js';

test('can upload cover', async ({ page, admin }) => {
	// given

	await new HomePage(page).nav.addBook();
	const addbookpage = new AddBookPage(page);

	await addbookpage.form.title.fill('Foo');
	await addbookpage.form.author.fill('Bar');
	await addbookpage.form.price.fill('12.5');
	await addbookpage.form.releasedAt.fill('2023-04-24');
	await addbookpage.form.cover.setInputFiles('src/test/resources/cover.jpg');

	// when
	await addbookpage.form.submit.click();
	// then
	const bookpage = new BookPage(page);
	await expect.soft(page).toHaveTitle('Foo');

	await expect.soft(bookpage.main.getByRole('heading', { name: 'Foo' })).toBeVisible();
	await expect.soft(bookpage.main.getByText('Bar')).toBeVisible();
	await expect.soft(bookpage.main.getByText('12,50 zł')).toBeVisible();
	await bookpage.delete();
});
