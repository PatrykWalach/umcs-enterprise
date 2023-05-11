/** @type {import('houdini').ConfigFile} */
const config = {
	schemaPath: 'src/main/resources/schema/schema.graphql',
	plugins: {
		'houdini-svelte': {
			framework: 'kit'
		}
	},
	defaultCachePolicy: 'CacheAndNetwork',
	scalars: {
		Upload: {
			type: 'File'
		},
		DateTime: {
			type: 'Date',
			marshal: /** @param {Date} date */ (date) => date.toISOString(),
			unmarshal: /** @param {string} value */ (value) => new Date(value)
		}
	}
};

export default config;
