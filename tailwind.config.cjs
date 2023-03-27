/** @type {import('tailwindcss').Config} */
module.exports = {
	content: ['./src/**/*.{html,svelte}'],
	theme: {
		extend: {}
	},
	daisyui: {
		themes: ['retro']
	},
	corePlugins: {
		fontSize: false
		// ...
	},
	plugins: [
		require('@tailwindcss/line-clamp'),
		require('daisyui'),
		require('tailwindcss-fluid-type')
	]
};
