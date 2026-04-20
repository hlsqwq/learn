<template>
  <section class="profile-page">
    <!-- 用户信息卡片 -->
    <div
      class="user-card-container flex flex-col md:flex-row items-center md:items-start gap-8 mb-12 bg-white/40 p-8 rounded-[2rem] border border-white shadow-sm">
      <div
        class="user-avatar-container w-32 h-32 rounded-full overflow-hidden shadow-xl border-4 border-white flex-shrink-0">
        <img :src="userAvatar" class="w-full h-full object-cover" alt="avatar" />
      </div>
      <div class="flex-1 text-center md:text-left">
        <h1 class="text-4xl font-black mb-2">{{ userName }}</h1>
        <div class="stats-container flex items-center justify-center md:justify-start gap-6 text-sm mb-6">
          <span class="text-gray-500 cursor-pointer hover:text-black">
            <strong class="text-black font-black text-lg">34.2k</strong> 粉丝
          </span>
          <span class="text-gray-500 cursor-pointer hover:text-black">
            <strong class="text-black font-black text-lg">128</strong> 关注
          </span>
        </div>
        <div class="action-buttons flex flex-wrap items-center justify-center md:justify-start gap-3">
          <button @click="handleCreatePlaylist"
            class="create-btn flex items-center gap-2 bg-rose-500 text-white px-5 py-2.5 rounded-xl text-xs font-black shadow-md hover:bg-rose-600 transition-colors">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24"
              stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 5v14m-7-7h14" />
            </svg>
            创建歌单
          </button>
          <button @click="handleUploadVideo"
            class="upload-btn flex items-center gap-2 bg-black text-white px-5 py-2.5 rounded-xl text-xs font-black shadow-md hover:bg-gray-800 transition-colors">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24"
              stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-8l-4-4m0 0L8 8m4-4v12" />
            </svg>
            上传视频
          </button>
        </div>
      </div>
    </div>

    <!-- 我喜欢区域 -->
    <div>
      <h2 class="section-title text-2xl font-black tracking-tight mb-6">我喜欢</h2>

      <!-- 分类切换 -->
      <div class="type-tabs flex space-x-2 bg-gray-200/50 p-1.5 rounded-2xl w-max mb-8">
        <button v-for="type in favoriteTypes" :key="type.id" @click="setFavType(type.id)" :class="`type-tab-button flex items-center gap-2 px-5 py-2 rounded-xl text-xs font-black transition-all ${favType === type.id
            ? 'bg-white text-rose-500 shadow-sm'
            : 'text-gray-500 hover:text-black'
          }`">
          <span class="text-lg">{{ type.icon }}</span>
          {{ type.label }}
        </button>
      </div>

      <!-- 收藏列表 -->
      <div class="favorites-grid grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        <div v-for="(item, i) in favoriteItems" :key="i"
          @click="$emit('play-song', { name: item.name, artist: userName, cover: item.cover })"
          class="favorite-item group flex items-center p-3 bg-white/60 hover:bg-white rounded-2xl transition-all cursor-pointer border border-white shadow-sm hover:shadow-md">
          <div class="favorite-cover w-14 h-14 rounded-xl overflow-hidden bg-gray-100 flex-shrink-0 relative">
            <img :src="item.cover" class="w-full h-full object-cover group-hover:scale-105 transition-transform"
              alt="" />
            <div
              class="favorite-overlay absolute inset-0 bg-black/20 opacity-0 group-hover:opacity-100 flex items-center justify-center transition-opacity">
              <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4 text-white ml-0.5" fill="currentColor"
                viewBox="0 0 24 24">
                <path d="M8 5v14l11-7z" />
              </svg>
            </div>
          </div>
          <div class="ml-4 flex-1 min-w-0">
            <h4 class="font-black text-sm truncate">{{ item.name }}</h4>
            <p class="text-[10px] text-gray-400 font-bold uppercase tracking-wider mt-0.5">今天添加</p>
          </div>
          <svg xmlns="http://www.w3.org/2000/svg"
            class="heart-icon w-4 h-4 text-rose-500 opacity-0 group-hover:opacity-100 transition-opacity"
            fill="currentColor" viewBox="0 0 24 24">
            <path
              d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" />
          </svg>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, computed } from 'vue'

defineEmits(['play-song'])

const favType = ref('songs')
const userName = ref('HLS_User_01')
const userAvatar = ref('https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200&h=200&fit=crop')

// 收藏类型
const favoriteTypes = ref([
  { id: 'songs', label: '单曲', icon: '🎵' },
  { id: 'playlists', label: '歌单', icon: '📋' },
  { id: 'albums', label: '专辑', icon: '💿' },
  { id: 'videos', label: '视频', icon: '🎬' }
])

// 根据类型生成收藏项
const favoriteItems = computed(() => {
  const typeMap = {
    songs: '单曲',
    playlists: '歌单',
    albums: '专辑',
    videos: '视频'
  }

  const currentType = typeMap[favType.value]

  return Array.from({ length: 9 }).map((_, i) => ({
    id: i,
    name: `我的私藏 ${currentType} ${i + 1}`,
    cover: `https://picsum.photos/seed/${favType.value}${i}/200/200`
  }))
})

const setFavType = (type) => {
  favType.value = type
}

const handleCreatePlaylist = () => {
  alert('正在打开创建歌单界面...')
}

const handleUploadVideo = () => {
  alert('正在打开上传视频界面...')
}
</script>

<style scoped>
.profile-page {
  min-height: 60vh;
}

.user-avatar-container {
  display: block;
}

.stats-container {
  display: flex;
  align-items: center;
}

.action-buttons {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
}

.create-btn,
.upload-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 900;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  transition: all 0.3s;
  background: none;
  border: none;
  cursor: pointer;
  color: white;
}

.create-btn {
  background: #F43F5E;
}

.create-btn:hover {
  background: #E11D48;
}

.upload-btn {
  background: #000;
}

.upload-btn:hover {
  background: #1F2937;
}

.section-title {
  font-weight: 900;
  letter-spacing: -0.02em;
}

.type-tabs {
  display: flex;
  gap: 8px;
  background: rgba(209, 213, 219, 0.5);
  padding: 6px;
  border-radius: 16px;
  width: max-content;
  margin-bottom: 32px;
}

.type-tab-button {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 20px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 900;
  transition: all 0.3s;
  background: none;
  border: none;
  cursor: pointer;
  font-family: inherit;
}

.favorites-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
}

.favorite-item {
  display: flex;
  align-items: center;
  padding: 12px;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 16px;
  transition: all 0.3s;
  cursor: pointer;
  border: 1px solid white;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.favorite-item:hover {
  background: white;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.favorite-cover {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  overflow: hidden;
  background: #F3F4F6;
  flex-shrink: 0;
  position: relative;
}

.favorite-overlay {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.2);
  opacity: 0;
  transition: opacity 0.3s;
}

.heart-icon {
  flex-shrink: 0;
}
</style>
