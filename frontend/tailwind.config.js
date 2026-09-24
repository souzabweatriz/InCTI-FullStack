/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      fontFamily: {
        display: ['Inter', 'system-ui', '-apple-system', 'BlinkMacSystemFont', 'Segoe UI', 'sans-serif'],
        ui: ['Inter', 'system-ui', '-apple-system', 'BlinkMacSystemFont', 'Segoe UI', 'sans-serif'],
      },
      colors: {
        primary: '#C89B3C',
        text: '#F3EFE4',
        'text-muted': '#9C9583',
        bg: '#0E0D0A',
        'bg-secondary': '#141310',
        accent: '#4B7C6F',
      },
    },
  },
  plugins: [],
}
 