import { expect, type Locator, type Page } from '@playwright/test';
class Layout {
	main: Locator;
	nav: Navigation;

	constructor(protected page: Page) {
		this.main = page.getByRole('main');
		this.nav = new Navigation(page, page.getByRole('navigation'));
	}
}
export default class HomePage extends Layout {
	bestsellers: Section;

	constructor(page: Page) {
		super(page);

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
			}),
			title
		);
	}
}

class Book {
	price: Locator;

	constructor(private page: Page, private loc: Locator, private title: string) {
		this.price = this.loc.getByTestId('price');
	}

	async navigate() {
		await this.loc.getByRole('link').click();
		await expect.soft(this.page).toHaveTitle(this.title);
		return new BookPage(this.page);
	}
}

class PurchasePage extends Layout {
	send: Locator;
	constructor(page: Page) {
		super(page);
		this.status = this.main.getByTestId('status');
		this.total = this.main.getByTestId('total');
		this.send = this.main.getByRole('button', { name: 'Send' });
	}
	status: Locator;
	total: Locator;
}

export class BasketPage extends Layout {
	async makePurchase() {
		await this.main.getByRole('button', { name: 'Make Purchase' }).click();
		await expect.soft(this.page).toHaveTitle('Purchase');
		return new PurchasePage(this.page);
	}

	book(title: string) {
		return this.main.getByRole('heading', {
			name: title
		});
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

	async addBook() {
		await this.locator.getByRole('button', { name: 'show menu' }).click();
		await this.locator.getByRole('link', { name: 'Add book' }).click();
		await expect.soft(this.page).toHaveTitle('Add book');
		return new AddBookPage(this.page);
	}

	async goToBasket() {
		await this.basketItems.click();
		await expect.soft(this.page).toHaveTitle('Basket');
		return new BasketPage(this.page);
	}

	login: Locator;
	register: Locator;

	constructor(private page: Page, private locator: Locator) {
		this.login = locator.getByRole('link', { name: 'login' });
		this.register = locator.getByRole('link', { name: 'register' });
		this.basketItems = locator.getByRole('link', { name: 'go to basket' });
	}
}

export class AddBookPage extends Layout {
	form: {
		title: Locator;
		author: Locator;
		price: Locator;
		releasedAt: Locator;
		cover: Locator;
		submit: Locator;
	};

	constructor(page: Page) {
		super(page);

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

export class BookPage extends Layout {
	private actions: Locator;

	async addToBasket() {
		await this.actions.getByRole('button', { name: 'Add to basket' }).click();
		await expect.soft(this.page.getByText('In the basket')).toBeVisible();
	}

	async delete() {
		await this.actions
			.getByRole('button', {
				name: 'Delete'
			})
			.click();
		await expect.soft(this.page).toHaveTitle('Home');
		return new HomePage(this.page);
	}

	constructor(page: Page) {
		super(page);
		this.actions = this.main.getByRole('form', { name: 'Actions' });
	}
}
