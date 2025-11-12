import Vue from 'vue'
import VueRouter from "vue-router";
Vue.use(VueRouter)

const router = new VueRouter({
  routes: [
    {
      path: '/',
      component: () => import('@/views/Layout.vue'),
      redirect: '/Article',
      children: [
        { path: '/Article', component: () => import('@/views/Article.vue') },
        { path: '/Like', component: () => import('@/views/Like.vue') },
        { path: '/User', component: () => import('@/views/User.vue') },
        { path: '/Collect', component: () => import('@/views/Collect.vue') },
      ],

    },
    { path: '/articleDetail', component: () => import('@/views/ArticleDetail.vue') },
  ]
})

export default router