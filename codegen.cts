import type { CodegenConfig } from '@graphql-codegen/cli';
import graphqlrc from './.graphqlrc.json';

const config: CodegenConfig = {
	...graphqlrc,
	schema: 'http://localhost:8080/graphql',
	generates: {
		'schema.graphql': {
			plugins: ['schema-ast']
		},
		'src/lib/gql/': {
			preset: 'client',
			plugins: [],
			config: {
				strictScalars: true,
				useTypeImports: true,
				scalars: {
					Date: 'string'
				}
			}
		}
	}
};

export default config;
