import { expect } from '@playwright/test';
import { test } from '../fixtures.js';

test('about page has expected h1', async ({ page }) => {
	await page.goto('/about');
	await expect.soft(page.getByRole('heading', { name: 'About this app' })).toBeVisible();
});
