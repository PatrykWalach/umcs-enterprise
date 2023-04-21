import { HoudiniClient } from '$houdini';
import { redirect } from '@sveltejs/kit';

function isErrorType(errorType: string) {
	return (error: unknown) =>
		typeof error === 'object' &&
		error &&
		'extensions' in error &&
		typeof error.extensions === 'object' &&
		error.extensions &&
		'errorType' in error.extensions &&
		error.extensions.errorType === errorType;
}

export default new HoudiniClient({
	url: 'http://localhost:8080/graphql',
	throwOnError: {
		operations: ['all'],
		error(errors) {
			if (errors.some(isErrorType('UNAUTHENTICATED'))) {
				throw redirect(303, '/login');
			}
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
