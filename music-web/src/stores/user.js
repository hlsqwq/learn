import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    isLoggedIn: false,
    userInfo: null,
    token: null
  }),
  actions: {
    login(token, userInfo) {
      this.isLoggedIn = true
      this.token = token
      this.userInfo = userInfo
      localStorage.setItem('token', token)
      localStorage.setItem('userInfo', JSON.stringify(userInfo))
    },
    logout() {
      this.isLoggedIn = false
      this.token = null
      this.userInfo = null
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
    },
    initUser() {
      const token = localStorage.getItem('token')
      const userInfo = localStorage.getItem('userInfo')
      if (token && userInfo) {
        this.isLoggedIn = true
        this.token = token
        this.userInfo = JSON.parse(userInfo)
      }
    },
    updateUserInfo(userInfo) {
      this.userInfo = userInfo
      localStorage.setItem('userInfo', JSON.stringify(userInfo))
    }
  }
})