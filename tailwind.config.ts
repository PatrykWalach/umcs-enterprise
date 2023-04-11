import { Config } from 'tailwindcss';
import defaultTheme from 'tailwindcss/defaultTheme';

const config: Config = {
	content: ['./src/**/*.{html,svelte}'],
	theme: {
		extend: {
			fontFamily: {
				sans: [['"Roboto Flex"', ...defaultTheme.fontFamily.sans], {}]
			}
		}
	},
	daisyui: {
		themes: ['retro']
	},
	corePlugins: {
		fontSize: false
		// ...
	},
	plugins: [require('daisyui'), require('tailwindcss-fluid-type')]
};

export default config;
