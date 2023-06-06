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

	async addToCart() {
		await this.loc.getByRole('button', { name: 'add to cart' }).click();
		await expect.soft(this.page.getByText('In the basket')).toBeVisible();
	}
}

export class CartPage {
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
	cartItems: Locator;
	async logout() {
		await this.locator.getByRole('button', { name: 'show menu' }).click();
		await this.locator.getByRole('button', { name: 'logout' }).click();
		await expect.soft(this.register).toBeVisible();
		await expect.soft(this.login).toBeVisible();
	}

	async goToCart() {
		await this.cartItems.click();
		await expect.soft(this.page).toHaveTitle('Cart');
	}

	login: Locator;
	register: Locator;

	constructor(private page: Page, private locator: Locator) {
		this.login = locator.getByRole('link', { name: 'login' });
		this.register = locator.getByRole('link', { name: 'register' });
		this.cartItems = locator.getByRole('link', { name: 'go to cart' });
	}
}

export class BookPage {
	main: Locator;
	nav: Navigation;

	async addToCart() {
		await this.main.getByRole('button', { name: 'To cart', exact: true }).click();
		await expect.soft(this.page.getByText('In the basket')).toBeVisible();

		return this;
	}

	constructor(private page: Page) {
		this.main = page.getByRole('main');
		this.nav = new Navigation(page, page.getByRole('navigation'));
	}
}
