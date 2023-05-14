import { graphql, setSession } from '$houdini';
import { redirect, type ServerLoad } from '@sveltejs/kit';
import * as z from 'zod';
import type { Actions } from './$houdini';

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
		client_id: 'newClient',
		client_secret: 'newClientSecret',
		redirect_uri: 'http://localhost:5173/login',
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

	event.cookies.set('enterprise-token', result.access_token, {
		expires: new Date(Date.now() + 1000 * result.expires_in),
		maxAge: 1000 * result.expires_in
	});

	setSession(event, {
		token: result.access_token ?? ''
	});

	throw redirect(303, event.url.searchParams.get('state') ?? '/');
	return {};
};

const login = graphql(`
	mutation Login($input: LoginInput!) {
		login(input: $input) {
			__typename
			... on LoginSuccess {
				token
			}
			... on LoginError {
				username
			}
		}
	}
`);

export const actions: Actions = {
	default: async (event) => {
		const { username, password } = Object.fromEntries(await event.request.formData());

		if (typeof username !== 'string') {
			throw new Error('No username');
		}

		if (typeof password !== 'string') {
			throw new Error('No password');
		}

		const response = await login.mutate(
			{
				input: {
					username,
					password
				}
			},
			{ event }
		);

		if (response.data?.login?.__typename === 'LoginSuccess') {
			event.cookies.set('enterprise-token', response.data?.login.token || '');
			throw redirect(303, '/');
		}

		if (response.data?.login?.__typename === 'LoginError') {
			return {
				username: {
					value: username,
					error: response.data?.login?.username
				}
			};
		}

		return response;
	}
};
