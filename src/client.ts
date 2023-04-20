import { HoudiniClient } from '$houdini';

export default new HoudiniClient({
	url: 'http://localhost:8080/graphql',
	fetchParams({ session }) {
		return {
			...(session?.token && {
				headers: {
					Authorization: `Bearer ${session.token}`
				}
			})
		};
	}
});
