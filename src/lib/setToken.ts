
import type { RequestEvent } from '@sveltejs/kit';
function graphql(){}
graphql(
	/* GraphQL */
	`
		fragment SetToken_token on Token {
			expiresAt @required
			value @required
		}
	`
);

export function setToken(event: RequestEvent, token: SetToken_token$data | null | undefined) {
	if (!token) {
		return;
	}

	event.cookies.set('enterprise-token', token.value, {
		path: '/',
		expires: token.expiresAt
	});

	setSession(event, {
		token: token.value
	});
}
