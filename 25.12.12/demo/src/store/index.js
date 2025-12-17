import Vue from 'vue'
import Vuex from 'vuex'


Vue.use(Vuex)

const store=new Vuex.Store({
  state:{
    num:100
  },
  mutations:{
    add(state,n){
      state.num+=n
    }
  },
  actions:{
    change_async(context,n){
      setTimeout(()=>{
        context.commit('add',n)
      },1000)
    },
  },
  modules:{
    user:require('./modules/user').default
  }
})
export default store 