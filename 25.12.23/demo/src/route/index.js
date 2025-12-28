import Vue from 'vue'
import Router from 'vue-router'
import layoutIndex from '@/view/layout/Index.vue'
import loginIndex from '@/view/login/index.vue'
import store from '@/store'
Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/',
      component: layoutIndex,
      redirect: '/home',
      children: [
        { path: '/home', component: () => import('@/view/layout/home.vue') },
        { path: '/classify', component: () => import('@/view/layout/classify.vue') },
        { path: '/cart', component: () => import('@/view/layout/cart.vue') },
        { path: '/my', component: () => import('@/view/layout/my.vue') }
      ]
    },
    {
      path: '/login',
      component: loginIndex
    },
    {
      path: '/pay',
      component: () => import('@/view/pay/index.vue')
    },
    {
      path: '/order',
      component: () => import('@/view/order/index.vue')
    },
    {
      path: '/prodetail/:id',
      component: () => import('@/view/proDetail/index.vue')
    },
    {
      path: '/search',
      component: () => import('@/view/search/index.vue')
    },
    {
      path: '/searchlist',
      component: () => import('@/view/search/list.vue')
    }

  ]
})

const list = ['/pay', '/order']
router.beforeEach((to, from, next) => {
  if (!list.includes(to.path)) {
    next()
    return
  }
  store.state.user.userInfo.token ? next() : next('/login')
})

export default router
