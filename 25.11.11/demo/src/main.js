import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)
const router = new VueRouter({
  routes: [
    { path: '/my', component: () => import('./views/MyDemo.vue') },
    { path: '/music', component: () => import('./views/MyMusic.vue') },
  ]
})


Vue.config.productionTip = false
Vue.directive('focus', {
  inserted: function (el) {
    el.focus()
  }
})

new Vue({
  render: h => h(App),
  router,
}).$mount('#app')
