import { HoudiniClient } from '$houdini';

export default new HoudiniClient({
	url: '/graphql',
	throwOnError: {
		operations: ['all'],
		error(errors) {
			console.error(errors);
		}
	},

	fetchParams({ session }) {
		return {
			headers: {
				...(session?.token && {
					Authorization: `Bearer ${session.token}`
				}),
				'graphql-require-preflight': ''
			}
		};
	}
});
