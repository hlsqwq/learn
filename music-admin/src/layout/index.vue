<template>
  <div class="flex h-screen w-full bg-[#fbfbfd] text-gray-800 overflow-hidden font-sans">
    <!-- 左侧极简侧边栏 - 毛玻璃效果 -->
    <aside class="w-64 flex flex-col glass z-10 shadow-[4px_0_24px_rgba(0,0,0,0.02)] transition-all duration-300">
      <div class="h-20 flex items-center px-8 border-b border-gray-100/50">
        <el-icon class="text-2xl text-blue-600 mr-3"><Headset /></el-icon>
        <h1 class="text-xl font-bold tracking-tight">Music Admin</h1>
      </div>
      
      <nav class="flex-1 py-6 px-4 space-y-2">
        <router-link
          v-for="route in menus"
          :key="route.path"
          :to="route.path"
          class="flex items-center px-4 py-3 rounded-2xl transition-all duration-300 group hover:bg-gray-100/80"
          active-class="bg-white shadow-[0_4px_12px_rgba(0,0,0,0.05)] text-blue-600 font-semibold"
        >
          <el-icon class="text-xl mr-4 text-gray-400 group-hover:text-blue-500 transition-colors"
                   :class="{'!text-blue-600': $route.path.includes(route.path)}">
            <component :is="route.meta.icon" />
          </el-icon>
          <span>{{ route.meta.title }}</span>
        </router-link>
      </nav>

      <!-- 底部用户信息区 -->
      <div class="p-6 border-t border-gray-100/50 flex items-center justify-between">
        <div class="flex items-center">
          <div class="w-10 h-10 rounded-full bg-gradient-to-tr from-blue-400 to-indigo-500 flex items-center justify-center text-white font-bold shadow-md">
            A
          </div>
          <div class="ml-3 flex flex-col">
            <span class="text-sm font-semibold">Admin</span>
            <span class="text-xs text-gray-400">系统管理员</span>
          </div>
        </div>
        <el-button text circle @click="handleLogout" class="hover:bg-red-50 hover:text-red-500">
          <el-icon><SwitchButton /></el-icon>
        </el-button>
      </div>
    </aside>

    <!-- 右侧内容区 -->
    <main class="flex-1 flex flex-col h-full overflow-hidden relative">
      <!-- 顶部毛玻璃导航 -->
      <header class="h-20 flex items-center justify-between px-10 glass sticky top-0 z-20 shadow-[0_4px_24px_rgba(0,0,0,0.02)]">
        <div class="flex items-center">
          <h2 class="text-2xl font-bold tracking-tight text-gray-800">{{ currentTitle }}</h2>
        </div>
        
        <div class="flex items-center space-x-6">
          <div class="relative flex items-center bg-gray-100/80 rounded-full px-4 py-2 w-64 focus-within:ring-2 ring-blue-500/50 transition-all">
            <el-icon class="text-gray-400"><Search /></el-icon>
            <input type="text" placeholder="全局搜索..." class="bg-transparent border-none outline-none ml-2 text-sm w-full placeholder-gray-400" />
          </div>
          <el-button circle class="!border-none !bg-gray-100/80 hover:!bg-gray-200 transition-colors">
            <el-icon><Bell /></el-icon>
          </el-button>
        </div>
      </header>

      <!-- 路由视图渲染区 -->
      <div class="flex-1 overflow-auto p-10 pb-20 scroll-smooth">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Headset, Music, Microphone, List, SwitchButton, Search, Bell } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

// 提取需要在侧边栏展示的路由菜单
const menus = computed(() => {
  const layoutRoute = router.options.routes.find(r => r.name === 'Layout')
  return layoutRoute?.children?.filter(r => r.meta && r.meta.title) || []
})

const currentTitle = computed(() => route.meta.title || '主页')

const handleLogout = () => {
  localStorage.removeItem('token')
  router.push('/login')
}
</script>

<style scoped>
/* 页面切换动画 */
.fade-transform-leave-active,
.fade-transform-enter-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}
.fade-transform-enter-from {
  opacity: 0;
  transform: translateY(10px) scale(0.99);
}
.fade-transform-leave-to {
  opacity: 0;
  transform: translateY(-10px) scale(0.99);
}

/* 自定义滚动条 */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}
::-webkit-scrollbar-track {
  background: transparent;
}
::-webkit-scrollbar-thumb {
  background: rgba(0,0,0,0.1);
  border-radius: 10px;
}
::-webkit-scrollbar-thumb:hover {
  background: rgba(0,0,0,0.2);
}
</style>
