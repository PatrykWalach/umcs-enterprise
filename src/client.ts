import { HoudiniClient } from '$houdini';
import { error, redirect } from '@sveltejs/kit';

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
		error: (errors, ctx) => {
			if (errors.some(isErrorType('UNAUTHENTICATED'))) {
				throw redirect(303, '/');
			}
			if (errors.some(isErrorType('NOT_FOUND'))) {
				throw error(404);
			}
			throw error(500, {
				message: `(${ctx.artifact.name}): ` + errors.map(({ message }) => message).join('. ') + '.'
			});
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
