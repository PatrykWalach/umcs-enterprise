import { Config } from "tailwindcss";

const config:  Config ={
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
		require('daisyui'),
		require('tailwindcss-fluid-type')
	]
} ;

export default config