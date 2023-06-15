import { getSession, setSession } from '$houdini';
import type { LoadEvent, RequestEvent } from '@sveltejs/kit';
import { BASKET_COOKIE, TOKEN_COOKIE } from './constants';

export function setBasket(event: RequestEvent, basket: string | null | undefined) {
	if (!basket) {
		event.cookies.delete(BASKET_COOKIE);
		return;
	}

	setSession(event, {
		token: event.cookies.get(TOKEN_COOKIE),
		basket
	});

	event.cookies.set(BASKET_COOKIE, basket, {
		path: '/'
	});
}


export async function getBasket(event: LoadEvent) {
	const session = await getSession(event);
	return 'basket' in session ? session.basket : undefined;
}