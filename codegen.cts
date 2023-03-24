import type { CodegenConfig } from '@graphql-codegen/cli';
import graphqlrc from './.graphqlrc.json';

const config: CodegenConfig = {
	...graphqlrc,
	config: {
		useTypeImports: true,
		avoidOptionals: { field: true, inputValue: false },
		skipTypename: true,
		immutableTypes: true
	},
	generates: {
		'src/lib/gql/': {
			preset: 'client',
			plugins: [],
			config: {
				scalars: {
					DateTime: 'string',
					Upload: 'File'
				}
			}
		}
	}
};

export default config;
