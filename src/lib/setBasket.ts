import { getSession, setSession } from '$houdini';
import type { LoadEvent, RequestEvent } from '@sveltejs/kit';
import { BASKET_COOKIE, TOKEN_COOKIE } from './constants';

export function setBasket(event: RequestEvent, basket: string | null | undefined) {
	setSession(event, {
		token: event.cookies.get(TOKEN_COOKIE),
		basket: basket ?? undefined
	});

	if (!basket) {
		event.cookies.delete(BASKET_COOKIE);
		return;
	}

	event.cookies.set(BASKET_COOKIE, basket, {
		path: '/',
		sameSite: 'lax',
		secure: false
	});
}

export async function getBasket(event: LoadEvent) {
	const session = await getSession(event);
	return 'basket' in session ? session.basket : undefined;
}
