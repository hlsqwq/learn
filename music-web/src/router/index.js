import { createRouter, createWebHistory } from 'vue-router'
import Discovery from '../views/Discovery.vue'
import Library from '../views/Library.vue'

const routes = [
  {
    path: '/',
    name: 'Discovery',
    component: Discovery
  },
  {
    path: '/library',
    name: 'Library',
    component: Library
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router