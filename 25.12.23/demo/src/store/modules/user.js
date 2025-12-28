
import { getUserInfo, setUserInfo, setHistory } from '@/utils/storage'

export default {
  namespaced: true,
  state () {
    return {
      userInfo: getUserInfo()
    }
  },
  mutations: {
    setToken (state, info) {
      setUserInfo(info)
      state.userInfo = info
    }
  },
  getters: {
    token (state) {
      return state.userInfo.token
    }

  },
  actions: {
    layout (context) {
      context.commit('setToken', {})
      context.commit('cart/setList', [], { root: true })
      setHistory([])
    }
  }
}
