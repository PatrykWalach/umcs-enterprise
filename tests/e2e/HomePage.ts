import { expect, type Locator, type Page } from '@playwright/test';

export default class HomePage {
	main: Locator;
	nav: Navigation;
	bestsellers: Section;

	constructor(private page: Page) {
		this.main = page.getByRole('main');
		this.nav = new Navigation(page, page.getByRole('navigation'));
		this.bestsellers = new Section(
			page,
			this.main.getByRole('region', {
				name: 'Bestsellers'
			})
		);
	}
}

class Section {
	constructor(private page: Page, private locator: Locator) {}
	public book(title: string) {
		return new Book(
			this.page,
			this.locator.getByRole('article', {
				name: title
			})
		);
	}
}

class Book {
	constructor(private page: Page, public loc: Locator) {}

	async addToBasket() {
		await this.loc.getByRole('button', { name: 'add to basket' }).click();
		await expect.soft(this.page.getByText('In the basket')).toBeVisible();
	}
}

export class BasketPage {
	main: Locator;
	book(title: string) {
		return this.main.getByRole('heading', {
			name: title
		});
	}

	constructor(private page: Page) {
		this.main = page.getByRole('main');
	}
}

export class Navigation {
	basketItems: Locator;
	async logout() {
		await this.locator.getByRole('button', { name: 'show menu' }).click();
		await this.locator.getByRole('button', { name: 'logout' }).click();
		await expect.soft(this.register).toBeVisible();
		await expect.soft(this.login).toBeVisible();
	}

	async goToBasket() {
		await this.basketItems.click();
		await expect.soft(this.page).toHaveTitle('Basket');
	}

	login: Locator;
	register: Locator;

	constructor(private page: Page, private locator: Locator) {
		this.login = locator.getByRole('link', { name: 'login' });
		this.register = locator.getByRole('link', { name: 'register' });
		this.basketItems = locator.getByRole('link', { name: 'go to basket' });
	}
}

export class AddBookPage {
	main: Locator;
	nav: Navigation;
	form: {
		title: Locator;
		author: Locator;
		price: Locator;
		releasedAt: Locator;
		cover: Locator;
		submit: Locator;
	};

	constructor(private page: Page) {
		this.main = page.getByRole('main');
		this.nav = new Navigation(page, page.getByRole('navigation'));

		const form = this.main.getByRole('group', { name: 'Add book' });

		this.form = {
			title: form.getByLabel('Title'),
			author: form.getByLabel('Author'),
			price: form.getByLabel('Price'),
			releasedAt: form.getByLabel('Release date'),
			cover: form.getByLabel('Cover'),
			submit: form.getByRole('button', { name: 'Submit' })
		};
	}
}

export class BookPage {
	main: Locator;
	nav: Navigation;

	async addToBasket() {
		await this.main
			.getByRole('form', { name: 'Actions' })
			.getByRole('button', { name: 'Add to basket' })
			.click();
		await expect.soft(this.page.getByText('In the basket')).toBeVisible();

		return this;
	}

	async delete() {
		await this.main
			.getByRole('button', {
				name: 'Delete'
			})
			.click();
		await expect.soft(this.page).toHaveTitle('Home');
	}

	constructor(private page: Page) {
		this.main = page.getByRole('main');
		this.nav = new Navigation(page, page.getByRole('navigation'));
	}
}
