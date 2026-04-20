import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/layout/index.vue'),
    redirect: '/song',
    children: [
      {
        path: 'song',
        name: 'Song',
        component: () => import('@/views/song/index.vue'),
        meta: { title: '歌曲管理', icon: 'Music' }
      },
      {
        path: 'singer',
        name: 'Singer',
        component: () => import('@/views/singer/index.vue'),
        meta: { title: '歌手管理', icon: 'Microphone' }
      },
      {
        path: 'song-list',
        name: 'SongList',
        component: () => import('@/views/song-list/index.vue'),
        meta: { title: '歌单管理', icon: 'List' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫：登录校验
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/')
  } else {
    next()
  }
})

export default router
