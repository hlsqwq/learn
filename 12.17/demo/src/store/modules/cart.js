/* eslint-disable import/no-extraneous-dependencies */
import axios from 'axios';

export default {
  namespaced: true,
  state() {
    return {
      list: [],
    };
  },
  mutations: {
    setList(state, payload) {
      state.list = payload;
    },
    changeCount(state, obj) {
      state.list.find((item) => item.id === obj.id).count = obj.count;
    },
  },
  actions: {
    async updateList(context) {
      const list = await axios({
        url: 'http://localhost:3000/cart',
        method: 'get',
      });
      context.commit('setList', list.data);
    },
    async changeCount(context, obj) {
      await axios({
        url: `http://localhost:3000/cart/${obj.id}`,
        method: 'patch',
        data: {
          count: obj.count,
        },
      });
      context.commit('changeCount', obj);
    },
  },
  getters: {
    totalCount(state) {
      return state.list.reduce((total, item) => total + item.count, 0);
    },
    totalPrice(state) {
      return state.list.reduce((total, item) => total + item.count * item.price, 0);
    },
  },
};
