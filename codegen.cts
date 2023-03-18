import type { CodegenConfig } from '@graphql-codegen/cli';
import graphqlrc from './.graphqlrc.json';

const config: CodegenConfig = {
	...graphqlrc,
	generates: {
		'src/lib/gql/': {
			preset: 'client',
			plugins: [],
			config: {
				useTypeImports: true,
				scalars: {
					DateTime: 'string',
					Upload: 'File'
				}
			}
		}
	}
};

export default config;
