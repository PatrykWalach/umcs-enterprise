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

class BasketBook extends Layout {
	quantity: Locator;
	async addMore() {
		const quantity = await this.nav.basketQuantity.textContent();
		await this.loc.getByRole('button', { name: 'Add more' }).click();
		await expect.soft(this.nav.basketQuantity).toHaveText(String(Number(quantity) + 1));
	}
	async remove() {
		const quantity = await this.nav.basketQuantity.textContent();
		await this.loc.getByRole('button', { name: 'Remove' }).click();
		await expect.soft(this.nav.basketQuantity).toHaveText(String(Number(quantity) - 1));
	}

	title: Locator;

	constructor(page: Page, public loc: Locator, title: string) {
		super(page);
		this.quantity = this.loc.getByTestId('quantity');
		this.title = this.loc.getByRole('heading', {
			name: title
		});
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
	async pay() {
		await this.main.getByRole('link', { name: 'Pay' }).click();
		return new PaypalPage(this.page);
	}

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

class PaypalPage {
	main: Locator;

	async paymentCard() {
		await this.main
			.getByRole('link', {
				name: 'Payment Card'
			})
			.click();

		await this.main.getByLabel('Card number').fill('4444 3333 2222 1111');
		await this.main.getByLabel('Valid thru').fill('12/29');
		await this.main.getByLabel('CCV').fill('123');
		await this.main.getByLabel('Name and Surname').fill('Foo Bar');
		await this.main.getByLabel('E-mail address').fill('email@example.com');

		await this.main
			.getByRole('button', {
				name: 'Pay'
			})
			.click();



	}

	constructor(private page: Page) {
		this.main = page.getByRole('main');
	}
}

export class BasketPage extends Layout {
	total: Locator;
	constructor(page: Page) {
		super(page);
		this.total = this.main.getByTestId('total');
	}
	async makePurchase() {
		await this.main.getByRole('button', { name: 'Make Purchase' }).click();
		await expect.soft(this.page).toHaveTitle('Purchase');
		await expect.soft(this.nav.basketQuantity).toHaveText('0');
		return new PurchasePage(this.page);
	}

	async next() {
		await this.main
			.getByRole('navigation')
			.getByRole('link', {
				name: 'Next'
			})
			.click();
	}

	book(title: string) {
		return new BasketBook(
			this.page,
			this.main.getByRole('article', {
				name: title
			}),
			title
		);
	}
}

export class Navigation {
	basketQuantity: Locator;

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
		await this.locator.getByRole('link', { name: 'go to basket' }).click();
		await expect.soft(this.page).toHaveTitle('Basket');
		return new BasketPage(this.page);
	}

	login: Locator;
	register: Locator;

	constructor(private page: Page, private locator: Locator) {
		this.login = locator.getByRole('link', { name: 'login' });
		this.register = locator.getByRole('link', { name: 'register' });

		this.basketQuantity = locator.getByTestId('basket-quantity');
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
		const quantity = await this.nav.basketQuantity.textContent();
		await this.actions.getByRole('button', { name: 'Add to basket' }).click();
		await expect.soft(this.page.getByText('In the basket')).toBeVisible();
		await expect.soft(this.nav.basketQuantity).toHaveText(String(Number(quantity) + 1));
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
