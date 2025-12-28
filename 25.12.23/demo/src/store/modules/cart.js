import { getCart, changeCart, delGoods } from '@/api/cart'
export default {
  namespaced: true,
  state () {
    return {
      list: []
    }
  },
  getters: {
    cartIds (state) {
      return state.list.map(i => {
        return i.checked && i.id
      })
    },
    cartTotal (state) {
      return state.list.reduce((pre, cur) => pre + cur.goods_num, 0)
    },
    isAll (state) {
      return state.list.every(item => item.checked)
    },
    payTotal (state) {
      return state.list.reduce((pre, cur) => {
        if (cur.checked === true) {
          return pre + 1
        }
        return pre
      }, 0)
    },
    payPrice (state) {
      return state.list.reduce((pre, cur) => {
        if (cur.checked === true) {
          return pre + cur.goods_num * cur.goods.goods_price_min
        }
        return pre
      }, 0)
    }
  },
  mutations: {
    toggleAll (state, flag) {
      state.list.forEach(i => {
        i.checked = flag
      })
    },
    setList (state, list) {
      state.list = list
    },
    setCartTotal (state, num) {
      state.cartTotal = num
    },
    setChecked (state, id) {
      state.list.forEach(i => {
        if (i.goods_id === id) {
          i.checked = !i.checked
        }
      })
    },
    setGoodCount (state, { id, count }) {
      state.list.forEach(i => {
        if (i.goods_id === id) {
          i.goods_num = count
        }
      })
    },
    delGoods (state, list) {
      state.list = state.list.filter(i => !list.includes(i.id))
    }
  },
  actions: {
    async getList (context) {
      const { data: { list, cartTotal } } = await getCart()
      list.forEach(element => {
        element.checked = true
      })
      context.commit('setList', list)
      context.commit('setCartTotal', cartTotal)
    },
    async changeCart (context, { goodsId, goodsNum, goodsSkuId }) {
      context.commit('setGoodCount', { id: goodsId, count: goodsNum })
      await changeCart(goodsId, goodsNum, goodsSkuId)
    },
    async delGoods (context, list) {
      context.commit('delGoods', list)
      await delGoods(list)
    }
  }
}
