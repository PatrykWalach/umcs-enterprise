/** @type {import('tailwindcss').Config} */
module.exports = {
	content: ['./src/**/*.{html,svelte}'],
	theme: {
		extend: {}
	},
	daisyui: {
		themes: ['retro']
	},
	plugins: [   require('@tailwindcss/line-clamp'),require('daisyui')]
};
