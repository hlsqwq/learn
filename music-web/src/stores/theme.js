import { defineStore } from 'pinia'
import { FastAverageColor } from 'fast-average-color'

const fac = new FastAverageColor()

export const useThemeStore = defineStore('theme', {
  state: () => ({
    primary: '#6366f1',
    secondary: '#8b5cf6',
    accent: '#ec4899'
  }),
  actions: {
    initTheme() {
      // 初始化主题颜色
      this.updateThemeFromImage('https://picsum.photos/800/800')
    },
    async updateThemeFromImage(imageUrl) {
      try {
        const color = await fac.getColorAsync(imageUrl)
        this.primary = color.hex
        this.secondary = this.lightenColor(color.hex, 20)
        this.accent = this.complementaryColor(color.hex)
        this.applyTheme()
      } catch (error) {
        console.error('Failed to update theme from image:', error)
      }
    },
    applyTheme() {
      document.documentElement.style.setProperty('--theme-primary', this.primary)
      document.documentElement.style.setProperty('--theme-secondary', this.secondary)
      document.documentElement.style.setProperty('--theme-accent', this.accent)
    },
    lightenColor(color, percent) {
      const num = parseInt(color.replace('#', ''), 16)
      const amt = Math.round(2.55 * percent)
      const R = (num >> 16) + amt
      const G = (num >> 8 & 0x00FF) + amt
      const B = (num & 0x0000FF) + amt
      return '#' + (0x1000000 + (R < 255 ? R < 1 ? 0 : R : 255) * 0x10000 + (G < 255 ? G < 1 ? 0 : G : 255) * 0x100 + (B < 255 ? B < 1 ? 0 : B : 255)).toString(16).slice(1)
    },
    complementaryColor(color) {
      const num = parseInt(color.replace('#', ''), 16)
      const R = 255 - (num >> 16)
      const G = 255 - (num >> 8 & 0x00FF)
      const B = 255 - (num & 0x0000FF)
      return '#' + (0x1000000 + R * 0x10000 + G * 0x100 + B).toString(16).slice(1)
    }
  }
})