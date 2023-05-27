import { load as flash } from 'sveltekit-flash-message/server';

export const load = (event) => flash(event);
