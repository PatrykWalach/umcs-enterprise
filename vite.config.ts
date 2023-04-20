import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vite';
import houdini from 'houdini/vite';

export default defineConfig({
	plugins: [sveltekit(), houdini()],
	server: {
		fs: {
			allow: ['.gql']
		}
	}
});
