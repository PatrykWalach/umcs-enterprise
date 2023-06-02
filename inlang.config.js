/**
 * @type { import("@inlang/core/config").DefineConfig }
 */
export async function defineConfig(env) {
	const [{ default: jsonPlugin }, { default: sdkPlugin }, { default: standardLintRules }] =
		await Promise.all([
			env.$import('https://cdn.jsdelivr.net/gh/samuelstroschein/inlang-plugin-json/dist/index.js'),
			env.$import('https://cdn.jsdelivr.net/npm/@inlang/sdk-js-plugin@0.6.1/dist/index.js'),
			env.$import('https://cdn.jsdelivr.net/gh/inlang/standard-lint-rules@2/dist/index.js')
		]);

	return {
		referenceLanguage: 'en',
		plugins: [
			standardLintRules(),
			jsonPlugin({ pathPattern: './languages/{language}.json' }),
			sdkPlugin({
				languageNegotiation: {
					strategies: [{ type: 'localStorage' }, { type: 'navigator' }]
				}
			})
		]
	};
}
