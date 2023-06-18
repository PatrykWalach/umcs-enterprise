import { setSession } from '$houdini';
import { BASKET_COOKIE, TOKEN_COOKIE } from '$lib/constants';
import { redirect, type ServerLoad } from '@sveltejs/kit';
import * as z from 'zod';

const token = z.object({
	expires_in: z.number(),
	access_token: z.string(),
	token_type: z.string()

	// token_type: z.literal('authorization_code'),
	// refresh_token:  z.string().optional(),
	// scope:  z.string().optional(),
});

export const load: ServerLoad = async (event) => {
	// return {}

	const code = event.url.searchParams.get('code');

	if (!code) {
		throw redirect(303, event.url.searchParams.get('state') ?? '/');
	}

	const body = new URLSearchParams({
		client_id: 'bookstore',
		client_secret: 'bookstoreSecret',
		redirect_uri: 'http://localhost:5173/login/callback',
		grant_type: 'authorization_code',
		code
		// client_secret: 'newClientSecret',
		// scope: 'openid profile read write',
	});

	const response = await event.fetch(`http://localhost:8080/oauth2/token`, {
		body: body,
		method: 'POST',
		headers: new Headers({
			// Accept: 'application/json, application/x-www-form-urlencoded',
			'Content-Type': 'application/x-www-form-urlencoded'
			// Authorization: `Basic ${Buffer.from(`${'newClient'}:${'newClientSecret'}`).toString(
			// 	'base64'
			// )}`
		})
	});

	const data = await response.json();

	const result = token.parse(data);

	event.cookies.set(TOKEN_COOKIE, result.access_token, {
		expires: new Date(Date.now() + 1000 * result.expires_in),
		maxAge: 1000 * result.expires_in,
		path: '/',
		sameSite: 'lax',
		secure: false
	});

	setSession(event, {
		token: result.access_token ?? '',
		basket: event.cookies.get(BASKET_COOKIE)
	});

	throw redirect(303, event.url.searchParams.get('state') ?? '/');
	return {};
};
