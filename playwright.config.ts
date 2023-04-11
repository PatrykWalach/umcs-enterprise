import { devices, PlaywrightTestConfig } from '@playwright/test';

const config: PlaywrightTestConfig = {
	webServer: {
		command: './gradlew bootRun',
		url: 'http://localhost:8080/graphiql',
		reuseExistingServer: !process.env.CI
	},
	snapshotDir: './__snapshots__',
	/* Maximum time one test can run for. */
	timeout: 10 * 1000,
	/* Run tests in files in parallel */
	fullyParallel: true,
	/* Fail the build on CI if you accidentally left test.only in the source code. */
	forbidOnly: !!process.env.CI,
	/* Retry on CI only */
	retries: process.env.CI ? 2 : 0,
	/* Opt out of parallel tests on CI. */
	workers: process.env.CI ? 1 : undefined,
	/* Reporter to use. See https://playwright.dev/docs/test-reporters */
	reporter: [['dot'], ['json', { outputFile: 'playwright-report/report.json' }]],
	/* Shared settings for all the projects below. See https://playwright.dev/docs/api/class-testoptions. */
	use: {
		/* Collect trace when retrying the failed test. See https://playwright.dev/docs/trace-viewer */
		trace: 'on-first-retry'
	},
	testDir: 'tests/e2e',
	projects: [
		{
			name: 'default',
			testDir: 'tests'
		},
		{
			name: 'no-js',
			use: { javaScriptEnabled: false }
		},
		{
			name: 'mobile',
			use: { javaScriptEnabled: false, ...devices['Pixel 5'] }
		}
	]
};

export default config;
