<template>
  <div class="ranking-view">
    <div class="flex items-end justify-between mb-8 border-b border-gray-200/50 pb-4">
      <h1 class="text-4xl font-black tracking-tight">排行榜</h1>
    </div>

    <div v-if="loading" class="text-center py-20">
      <div class="animate-spin-slow w-12 h-12 mx-auto text-rose-500 mb-4">
        <svg xmlns="http://www.w3.org/2000/svg" class="w-12 h-12" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 3v4M3 5h4M6 17v4m-2-2h4m5-16l2.286 6.857L21 12l-5.714 2.143L13 21l-2.286-6.857L5 12l5.714-2.143L13 3z" />
        </svg>
      </div>
      <h2 class="text-xl font-black text-gray-400">加载中...</h2>
    </div>

    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div v-for="category in categories" :key="category.id" class="bg-white/60 backdrop-blur-sm rounded-3xl p-6 border border-white shadow-sm">
        <div class="flex items-center justify-between mb-6">
          <h2 class="text-lg font-black flex items-center gap-2">
            <span class="w-2 h-6 bg-rose-500 rounded-full"></span>
            {{ category.content }}
          </h2>
        </div>
        <div class="space-y-3">
          <div v-for="(song, index) in category.songs" :key="song.id"
            @click="handlePlaySong(song)"
            class="ranking-item group cursor-pointer hover:bg-white/40 p-2 -mx-2 rounded-xl transition-all flex items-center">
            <span :class="[
              'rank-number text-xl font-black italic w-8 transition-colors',
              index === 0 ? 'text-amber-400' : index === 1 ? 'text-gray-400' : index === 2 ? 'text-amber-600' : 'text-gray-300 group-hover:text-rose-500'
            ]">{{ String(index + 1).padStart(2, '0') }}</span>
            <img :src="song.avatar || 'https://picsum.photos/100/100?random=' + song.id" :alt="song.name"
              class="w-10 h-10 rounded-lg object-cover ml-2" />
            <div class="flex-1 ml-3 min-w-0">
              <p class="text-sm font-black truncate">{{ song.name }}</p>
              <p class="text-[9px] text-gray-400 font-bold truncate">{{ song.album || song.introduction || '未知' }}</p>
            </div>
            <svg xmlns="http://www.w3.org/2000/svg"
              class="w-4 h-4 text-rose-500 opacity-0 group-hover:opacity-100 transition-all flex-shrink-0" fill="currentColor"
              viewBox="0 0 24 24">
              <path d="M8 5v14l11-7z" />
            </svg>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { contentAPI } from '../services/api'

defineEmits(['play-song'])

const loading = ref(true)
const categories = ref([])

const handlePlaySong = (song) => {
  // Emit or directly play - for now just log
  console.log('Play song:', song)
}

onMounted(async () => {
  try {
    const categoriesData = await contentAPI.getCategoryList()
    if (categoriesData.code === 200) {
      const cats = categoriesData.data || []
      
      const songsPromises = cats.slice(0, 6).map(async (cat) => {
        try {
          const songsData = await contentAPI.getSongPage(cat.id, { num: 1, size: 5 })
          return {
            ...cat,
            songs: songsData.code === 200 ? (songsData.data?.item || []) : []
          }
        } catch {
          return { ...cat, songs: [] }
        }
      })
      
      categories.value = await Promise.all(songsPromises)
    }
  } catch (error) {
    console.error('Failed to fetch ranking data:', error)
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.ranking-view {
  min-height: 60vh;
}

.ranking-item {
  display: flex;
  align-items: center;
}

.rank-number {
  flex-shrink: 0;
}

.animate-spin-slow {
  animation: spin 8s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}
</style>
