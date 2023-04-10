module.exports = {
	root: true,
	parser: '@typescript-eslint/parser',
	extends: ['eslint:recommended', 'turbo', 'plugin:@typescript-eslint/recommended', 'prettier'],
	plugins: ['svelte3', '@typescript-eslint'],
	ignorePatterns: ['*.cjs'],
	overrides: [
		{ files: ['*.svelte'], processor: 'svelte3/svelte3' },
		{
			files: ['*.js', '*.ts'],
			processor: '@graphql-eslint/graphql'
		},
		{
			files: ['*.graphqls'],
			extends: ['plugin:@graphql-eslint/schema-all', 'plugin:@graphql-eslint/relay'],
			rules: {
				'@graphql-eslint/require-description': 'off',
				'@graphql-eslint/require-field-of-type-query-in-mutation-result': 'off',
				'@graphql-eslint/strict-id-in-types': [
					'error',
					{
						exceptions: {
							suffixes: ['Connection', 'Edge', 'Result'],
							types: ['PageInfo', 'Basket']
						}
					}
				]
			}
		},
		{
			files: ['*.graphql', '*.gql'],
			extends: ['plugin:@graphql-eslint/operations-all'],
			rules: {
				'@graphql-eslint/match-document-filename': 'off',
				'@graphql-eslint/naming-convention': [
					'error',
					{
						VariableDefinition: 'camelCase',
						OperationDefinition: {
							style: 'PascalCase'
						},
						FragmentDefinition: {
							forbiddenPrefixes: ['Fragment'],
							forbiddenSuffixes: ['Fragment']
						}
					}
				]
			}
		}
	],
	settings: {
		'svelte3/typescript': () => require('typescript')
	},
	parserOptions: {
		sourceType: 'module',
		ecmaVersion: 2020
	},
	env: {
		browser: true,
		es2017: true,
		node: true
	}
};
