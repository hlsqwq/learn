<template>
  <div class="app-container">
    <!-- 左侧侧边栏 -->
    <aside class="sidebar">
      <div class="sidebar-content">
        <!-- Logo -->
        <div class="logo-section" @click="activeTab = 'new'">
          <div class="logo-icon">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6 text-white" fill="none" viewBox="0 0 24 24"
              stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M9 19V6l12-3v13M9 19c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zm12-3c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zM9 10l12-3" />
            </svg>
          </div>
          <span class="logo-text">YXMusic</span>
        </div>

        <!-- 盒子型导航 -->
        <div class="px-4 flex items-center justify-center space-x-3 mb-6">
          <BoxNavItem label="发现" iconName="layout-grid" :active="activeTab === 'new'" @click="handleNavClick('new')" />
          <BoxNavItem label="榜单" iconName="trophy" :active="activeTab === 'ranking'"
            @click="handleNavClick('ranking')" />
        </div>

        <div class="px-6 mb-2">
          <div class="h-[1px] w-full bg-gray-200/60"></div>
        </div>

        <!-- 个人媒体库导航 -->
        <div class="px-4 space-y-1 mb-6">
          <NavItem label="我喜欢" iconName="heart" :active="activeTab === 'library' && subTab === 'favorite'"
            @click="handleNavClick('library', 'favorite')" />
          <NavItem label="播放记录" iconName="history" :active="activeTab === 'library' && subTab === 'history'"
            @click="handleNavClick('library', 'history')" />
        </div>

        <div class="divider"></div>

        <!-- 歌单区域 -->
        <div class="playlist-section">
          <div class="playlist-tabs">
            <button @click="playlistTab = 'created'" :class="['tab-button', { active: playlistTab === 'created' }]">
              自建
            </button>
            <button @click="playlistTab = 'bookmarked'"
              :class="['tab-button', { active: playlistTab === 'bookmarked' }]">
              收藏
            </button>
          </div>

          <div class="playlist-list">
            <div v-for="list in sidebarPlaylists[playlistTab]" :key="list.id"
              @click="handlePlaySong({ name: list.name, artist: userName, cover: list.cover })" class="playlist-item">
              <img :src="list.cover" class="playlist-cover" alt="cover" />
              <div class="playlist-info">
                <p class="playlist-name">{{ list.name }}</p>
                <p class="playlist-count">{{ list.count }} 首</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 底部用户信息 -->
      <div class="user-section">
        <div @click="activeTab = 'profile'" class="user-card">
          <img :src="userAvatar" class="user-avatar" alt="user" />
          <div class="user-info">
            <p class="user-name">{{ userName }}</p>
            <p class="user-action">进入主页</p>
          </div>
          <svg xmlns="http://www.w3.org/2000/svg" class="chevron-right" fill="none" viewBox="0 0 24 24"
            stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
          </svg>
        </div>
      </div>
    </aside>

    <!-- 主内容区 -->
    <main class="main-content">
      <!-- 顶部搜索栏 -->
      <header class="header">
        <div class="search-container group">
          <svg xmlns="http://www.w3.org/2000/svg" class="search-icon" fill="none" viewBox="0 0 24 24"
            stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
          </svg>
          <input type="text" placeholder="搜索任何好音乐..." v-model="searchQuery" @keyup.enter="handleSearch"
            class="search-input" />
        </div>
      </header>

      <!-- 内容区域 -->
      <div class="content-area">
        <div class="content-wrapper animate-in fade-in duration-500">
          <DiscoveryView v-if="activeTab === 'new'" @play-song="handlePlaySong" />
          <LibraryView v-if="activeTab === 'profile'" @play-song="handlePlaySong" />
          <RankingView v-if="activeTab === 'ranking'" />
          <HistoryView v-if="activeTab === 'library' && subTab === 'history'" />
          <FavoriteView v-if="activeTab === 'library' && subTab === 'favorite'" />
        </div>
      </div>

      <!-- 底部播放器 -->
      <footer class="player-bar">
        <div class="player-center">
          <div class="control-buttons">
            <button class="control-btn skip-btn">
              <svg xmlns="http://www.w3.org/2000/svg" class="icon-size" fill="none" viewBox="0 0 24 24"
                stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M12.066 11.2a1 1 0 000 1.6l5.334 4A1 1 0 0019 16V8a1 1 0 00-1.6-.8l-5.333 4zM4.066 11.2a1 1 0 000 1.6l5.334 4A1 1 0 0011 16V8a1 1 0 00-1.6-.8l-5.334 4z" />
              </svg>
            </button>
            <button @click="togglePlay" class="play-pause-btn">
              <svg v-if="isPlaying" xmlns="http://www.w3.org/2000/svg" class="icon-size" fill="white"
                viewBox="0 0 24 24">
                <path d="M6 4h4v16H6V4zm8 0h4v16h-4V4z" />
              </svg>
              <svg v-else xmlns="http://www.w3.org/2000/svg" class="icon-size play-icon" fill="white"
                viewBox="0 0 24 24">
                <path d="M8 5v14l11-7z" />
              </svg>
            </button>
            <button class="control-btn skip-btn">
              <svg xmlns="http://www.w3.org/2000/svg" class="icon-size" fill="none" viewBox="0 0 24 24"
                stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M11.933 12.8a1 1 0 000-1.6L6.6 7.2A1 1 0 005 8v8a1 1 0 001.6.8l5.333-4zM19.933 12.8a1 1 0 000-1.6l-5.333-4A1 1 0 0013 8v8a1 1 0 001.6.8l5.333-4z" />
              </svg>
            </button>
          </div>
          <div class="progress-section">
            <span class="time-text">0:00</span>
            <div class="progress-bar group">
              <div class="progress-fill"></div>
            </div>
            <span class="time-text">4:56</span>
          </div>
        </div>
      </footer>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import BoxNavItem from './components/BoxNavItem.vue'
import NavItem from './components/NavItem.vue'
import { usePlayerStore } from './stores/player'
import DiscoveryView from './views/Discovery.vue'
import LibraryView from './views/Library.vue'
import RankingView from './views/Ranking.vue'
import HistoryView from './views/History.vue'
import FavoriteView from './views/Favorite.vue'

const playerStore = usePlayerStore()

const activeTab = ref('new')
const subTab = ref('favorite')
const playlistTab = ref('created')
const searchQuery = ref('')
const isPlaying = ref(false)

const userName = ref("HLS_User_01")
const userAvatar = ref("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200&h=200&fit=crop")

// 侧边栏歌单数据
const sidebarPlaylists = ref({
  created: Array.from({ length: 15 }).map((_, i) => ({
    id: `c${i}`, name: `我的歌单 ${i + 1}`, count: Math.floor(Math.random() * 50) + 10, cover: `https://picsum.photos/seed/c${i}/100/100`
  })),
  bookmarked: Array.from({ length: 12 }).map((_, i) => ({
    id: `b${i}`, name: `精选收藏 ${i + 1}`, count: Math.floor(Math.random() * 100) + 20, cover: `https://picsum.photos/seed/b${i}/100/100`
  }))
})

const currentSong = computed(() => playerStore.currentSong)

const handleNavClick = (tab, sub = null) => {
  activeTab.value = tab
  if (sub) subTab.value = sub
}

const handlePlaySong = (song) => {
  playerStore.setCurrentSong(song)
  isPlaying.value = true
}

const togglePlay = () => {
  isPlaying.value = !isPlaying.value
}

const handleSearch = () => {
  console.log('搜索:', searchQuery.value)
}

onMounted(() => {
  // 初始化
})
</script>

<style scoped>
.app-container {
  display: flex;
  height: 100vh;
  background-color: #F5F5F7;
  color: #1D1D1F;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

/* 侧边栏 */
.sidebar {
  width: 224px;
  background: rgba(243, 244, 246, 0.4);
  backdrop-filter: blur(24px);
  -webkit-backdrop-filter: blur(24px);
  border-right: 1px solid rgba(229, 231, 235, 0.5);
  display: flex;
  flex-direction: column;
  z-index: 20;
  flex-shrink: 0;
}

.sidebar-content {
  flex: 1;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  padding: 0;
}

/* Logo 区域 */
.logo-section {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #F43F5E;
  padding: 32px 16px;
  cursor: pointer;
}

.logo-icon {
  background: #F43F5E;
  padding: 8px;
  border-radius: 12px;
  box-shadow: 0 10px 15px -3px rgba(244, 63, 94, 0.3);
  flex-shrink: 0;
}

.logo-text {
  font-weight: 900;
  font-style: italic;
  letter-spacing: -0.05em;
  font-size: 20px;
  color: #000;
}

.divider {
  height: 1px;
  width: calc(100% - 48px);
  background: rgba(209, 213, 219, 0.6);
  margin: 0 auto 24px;
}

/* 歌单区域 */
.playlist-section {
  padding: 0 16px;
  flex: 1;
  display: flex;
  flex-direction: column;
  min-height: 200px;
}

.playlist-tabs {
  display: flex;
  background: rgba(209, 213, 219, 0.5);
  padding: 4px;
  border-radius: 12px;
  margin-bottom: 12px;
  flex-shrink: 0;
}

.tab-button {
  flex: 1;
  padding: 6px 0;
  font-size: 11px;
  font-weight: 900;
  border-radius: 8px;
  transition: all 0.3s;
  background: transparent;
  color: #9CA3AF;
  cursor: pointer;
  border: none;
}

.tab-button.active {
  background: white;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  color: #000;
}

.tab-button:hover:not(.active) {
  color: #6B7280;
}

.playlist-list {
  flex: 1;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding-right: 4px;
  padding-bottom: 16px;
}

.playlist-item {
  display: flex;
  align-items: center;
  padding: 8px;
  border-radius: 12px;
  transition: all 0.3s;
  cursor: pointer;
}

.playlist-item:hover {
  background: rgba(255, 255, 255, 0.6);
}

.playlist-cover {
  width: 32px;
  height: 32px;
  border-radius: 6px;
  background: #E5E7EB;
  flex-shrink: 0;
  object-fit: cover;
  opacity: 0.9;
}

.playlist-item:hover .playlist-cover {
  opacity: 1;
}

.playlist-info {
  margin-left: 12px;
  min-width: 0;
}

.playlist-name {
  font-size: 11px;
  font-weight: 700;
  color: #374151;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  transition: color 0.3s;
}

.playlist-item:hover .playlist-name {
  color: #000;
}

.playlist-count {
  font-size: 9px;
  font-weight: 700;
  color: #9CA3AF;
}

/* 用户区域 */
.user-section {
  padding: 16px;
  border-top: 1px solid rgba(209, 213, 219, 0.8);
  flex-shrink: 0;
}

.user-card {
  padding: 10px 12px;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid transparent;
}

.user-card:hover {
  background: white;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.user-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  object-fit: cover;
  flex-shrink: 0;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.user-info {
  flex: 1;
  min-width: 0;
}

.user-name {
  font-size: 11px;
  font-weight: 900;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.user-action {
  font-size: 8px;
  color: #9CA3AF;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: -0.02em;
  margin-top: 2px;
}

.chevron-right {
  width: 14px;
  height: 14px;
  color: #9CA3AF;
  flex-shrink: 0;
}

/* 主内容区 */
.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  position: relative;
  overflow: hidden;
}

/* 顶部搜索栏 */
.header {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 40px;
  background: rgba(245, 245, 247, 0.8);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  position: sticky;
  top: 0;
  z-index: 10;
}

.search-container {
  position: relative;
  width: 100%;
  max-width: 28rem;
}

.search-icon {
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  width: 18px;
  height: 18px;
  color: #9CA3AF;
  transition: color 0.3s;
}

.search-container:focus-within .search-icon {
  color: #F43F5E;
}

.search-input {
  width: 100%;
  background: white;
  border: none;
  border-radius: 16px;
  padding: 10px 16px 10px 44px;
  font-size: 14px;
  font-weight: 500;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  outline: none;
  transition: box-shadow 0.3s;
}

.search-input:focus {
  box-shadow: 0 0 0 2px rgba(244, 63, 94, 0.2);
}

.search-input::placeholder {
  color: #9CA3AF;
}

/* 内容区域 */
.content-area {
  flex: 1;
  overflow-y: auto;
  padding-bottom: 112px;
  padding: 0 40px;
}

.content-wrapper {
  max-width: 72rem;
  margin: 0 auto;
  padding: 24px 0;
}

/* 底部播放器 */
.player-bar {
  height: 80px;
  background: rgba(245, 245, 247, 0.8);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-top: 1px solid rgba(229, 231, 235, 0.6);
  position: fixed;
  bottom: 0;
  left: 224px;
  right: 0;
  z-index: 30;
  padding: 0 40px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 -1px 10px rgba(0, 0, 0, 0.02);
}

.player-center {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
  max-width: 28rem;
  padding: 0 40px;
}

.control-buttons {
  display: flex;
  align-items: center;
  gap: 40px;
  margin-bottom: 6px;
}

.control-btn {
  background: none;
  border: none;
  cursor: pointer;
  transition: color 0.3s;
  padding: 0;
}

.skip-btn {
  color: #9CA3AF;
}

.skip-btn:hover {
  color: #000;
}

.icon-size {
  width: 18px;
  height: 18px;
}

.play-pause-btn {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: #000;
  color: white;
  transition: transform 0.2s;
  border: none;
  cursor: pointer;
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.3);
}

.play-pause-btn:hover {
  transform: scale(1.05);
}

.play-pause-btn:active {
  transform: scale(0.95);
}

.play-icon {
  margin-left: 2px;
}

.progress-section {
  width: 100%;
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 9px;
  color: #9CA3AF;
  font-weight: 900;
  letter-spacing: -0.03em;
}

.progress-bar {
  flex: 1;
  height: 4px;
  background: rgba(209, 213, 219, 0.8);
  border-radius: 999px;
  position: relative;
  overflow: hidden;
  cursor: pointer;
}

.progress-fill {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 33.33%;
  background: #F43F5E;
  transition: background 0.3s;
}

.progress-bar:hover .progress-fill {
  background: #E11D48;
}

.time-text {
  flex-shrink: 0;
}

/* 动画 */
.animate-in {
  animation: fadeIn 0.4s cubic-bezier(0.16, 1, 0.3, 1) forwards;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(6px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 自定义滚动条 */
.sidebar-content::-webkit-scrollbar,
.content-area::-webkit-scrollbar,
.playlist-list::-webkit-scrollbar {
  width: 4px;
  height: 4px;
}

.sidebar-content::-webkit-scrollbar-track,
.content-area::-webkit-scrollbar-track,
.playlist-list::-webkit-scrollbar-track {
  background: transparent;
}

.sidebar-content::-webkit-scrollbar-thumb,
.content-area::-webkit-scrollbar-thumb,
.playlist-list::-webkit-scrollbar-thumb {
  background: rgba(0, 0, 0, 0.1);
  border-radius: 10px;
}

.sidebar-content:hover::-webkit-scrollbar-thumb,
.content-area:hover::-webkit-scrollbar-thumb,
.playlist-list:hover::-webkit-scrollbar-thumb {
  background: rgba(0, 0, 0, 0.2);
}
</style>