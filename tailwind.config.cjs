/** @type {import('tailwindcss').Config} */
module.exports = {
	content: ['./src/**/*.{html,svelte}'],
	theme: {
		extend: {}
	},
	daisyui: {
		themes: ['retro']
	},
	plugins: [require('daisyui')]
};
