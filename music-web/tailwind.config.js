/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: 'var(--theme-primary)',
        secondary: 'var(--theme-secondary)',
        accent: 'var(--theme-accent)',
      },
      fontFamily: {
        inter: ['Inter', 'sans-serif'],
        pingfang: ['PingFang SC', 'sans-serif'],
      },
    },
  },
  plugins: [],
}