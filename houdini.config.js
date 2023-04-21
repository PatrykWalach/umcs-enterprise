/** @type {import('houdini').ConfigFile} */
const config = {
	watchSchema: {
		url: 'http://localhost:8080/graphql'
	},
	plugins: {
		'houdini-svelte': {
			framework: 'kit'
		}
	},
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
