<template>
  <div class="history-view">
    <div class="flex items-end justify-between mb-8 border-b border-gray-200/50 pb-4">
      <h1 class="text-4xl font-black tracking-tight">最近播放</h1>
      <span class="text-sm text-gray-500">{{ songs.length }} 首歌曲</span>
    </div>

    <div v-if="loading" class="text-center py-20">
      <div class="animate-spin-slow w-12 h-12 mx-auto text-rose-500 mb-4">
        <svg xmlns="http://www.w3.org/2000/svg" class="w-12 h-12" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
      </div>
      <h2 class="text-xl font-black text-gray-400">加载中...</h2>
    </div>

    <div v-else-if="songs.length === 0" class="text-center py-20">
      <svg xmlns="http://www.w3.org/2000/svg" class="w-12 h-12 mx-auto text-gray-300 mb-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
      </svg>
      <h2 class="text-xl font-black">最近播放记录</h2>
      <p class="text-gray-500 mt-2">您还没有播放记录</p>
    </div>

    <div v-else class="space-y-1">
      <div v-for="(song, index) in songs" :key="song.id"
        @click="handlePlaySong(song)"
        class="song-item group cursor-pointer hover:bg-white/40 p-4 rounded-xl transition-all flex items-center">
        <span class="w-8 text-center text-gray-400 font-bold">{{ index + 1 }}</span>
        <img :src="song.avatar || 'https://picsum.photos/100/100?random=' + song.id" :alt="song.name"
          class="w-12 h-12 rounded-lg object-cover ml-2" />
        <div class="flex-1 ml-4 min-w-0">
          <p class="font-black truncate">{{ song.name }}</p>
          <p class="text-sm text-gray-500 truncate">{{ song.album || song.introduction || '未知专辑' }}</p>
        </div>
        <div class="text-right mr-4">
          <p class="text-xs text-gray-400">{{ song.duration || '00:00' }}</p>
        </div>
        <svg xmlns="http://www.w3.org/2000/svg"
          class="w-5 h-5 text-rose-500 opacity-0 group-hover:opacity-100 transition-all flex-shrink-0" fill="currentColor"
          viewBox="0 0 24 24">
          <path d="M8 5v14l11-7z" />
        </svg>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { contentAPI, mockAPI } from '../services/api'

defineEmits(['play-song'])

const loading = ref(true)
const songs = ref([])

const handlePlaySong = (song) => {
  console.log('Play song:', song)
}

onMounted(async () => {
  try {
    const res = await mockAPI.getFavoriteSongs()
    if (res.code === 200) {
      songs.value = res.data?.item || []
    }
  } catch (error) {
    console.error('Failed to fetch history songs:', error)
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.history-view {
  min-height: 60vh;
}

.song-item {
  display: flex;
  align-items: center;
}

.animate-spin-slow {
  animation: spin 8s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}
</style>
