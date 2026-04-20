<template>
  <div v-if="isOpen" class="fixed inset-0 bg-black/80 backdrop-blur-sm z-50 flex items-center justify-center p-4">
    <div class="glassmorphism w-full max-w-2xl p-6 rounded-2xl">
      <div class="flex items-center mb-6">
        <div class="relative flex-1">
          <input 
            type="text" 
            v-model="searchQuery" 
            placeholder="搜索歌曲、歌手、专辑或歌单..." 
            class="w-full bg-white/10 border border-white/20 rounded-full py-3 px-5 pl-12 focus:outline-none focus:ring-2 focus:ring-primary"
            @input="handleSearch"
            ref="searchInput"
          />
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400 absolute left-4 top-1/2 transform -translate-y-1/2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
          </svg>
        </div>
        <button class="ml-4 hover:text-primary transition-colors" @click="close">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>

      <div v-if="searchResults.length > 0" class="space-y-4">
        <div v-for="(result, index) in searchResults" :key="index"
          @click="handleResultClick(result)"
          class="flex items-center space-x-4 p-3 rounded-lg hover:bg-white/10 transition-colors cursor-pointer">
          <div class="w-12 h-12 rounded-md overflow-hidden flex-shrink-0">
            <img :src="result.image || 'https://picsum.photos/100/100?random=' + index" :alt="result.name" class="w-full h-full object-cover" />
          </div>
          <div class="flex-1 min-w-0">
            <div class="font-medium truncate">{{ result.name }}</div>
            <div class="text-sm text-gray-400 truncate">{{ result.artist || result.type }}</div>
          </div>
          <span class="text-[10px] text-gray-500 bg-white/10 px-2 py-1 rounded-full">{{ result.type }}</span>
        </div>
      </div>
      <div v-else-if="loading" class="text-center py-10 text-gray-400">
        <p>搜索中...</p>
      </div>
      <div v-else-if="searchQuery" class="text-center py-10 text-gray-400">
        <p>没有找到相关结果</p>
      </div>
      <div v-else class="text-center py-10 text-gray-400">
        <p>输入关键词开始搜索</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, nextTick } from 'vue'
import { searchAPI } from '../services/api'

const props = defineProps({
  isOpen: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['close', 'play-song'])

const searchQuery = ref('')
const searchResults = ref([])
const searchInput = ref(null)
const loading = ref(false)
let searchTimer = null

const close = () => {
  emit('close')
  searchQuery.value = ''
  searchResults.value = []
}

const handleSearch = () => {
  if (searchTimer) clearTimeout(searchTimer)
  
  if (!searchQuery.value.trim()) {
    searchResults.value = []
    return
  }
  
  searchTimer = setTimeout(async () => {
    loading.value = true
    try {
      const res = await searchAPI.findAll(searchQuery.value)
      if (res.code === 200 && res.data) {
        const results = []
        for (const [type, items] of Object.entries(res.data)) {
          if (Array.isArray(items)) {
            items.forEach(item => {
              results.push({
                ...item,
                type,
                name: item.name || item.title,
                image: item.avatar || item.cover,
                artist: item.artist || item.singer || item.introduction
              })
            })
          }
        }
        searchResults.value = results
      } else {
        searchResults.value = []
      }
    } catch (error) {
      console.error('Search failed:', error)
      searchResults.value = []
    } finally {
      loading.value = false
    }
  }, 300)
}

const handleResultClick = (result) => {
  emit('play-song', {
    name: result.name,
    artist: result.artist || '未知',
    cover: result.image,
    ...result
  })
  close()
}

onMounted(() => {
  if (props.isOpen) {
    nextTick(() => {
      searchInput.value?.focus()
    })
  }
})

watch(() => props.isOpen, (newVal) => {
  if (newVal) {
    nextTick(() => {
      searchInput.value?.focus()
    })
  }
})
</script>

<style scoped>
/* 搜索蒙层动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.scale-enter-active,
.scale-leave-active {
  transition: transform 0.3s ease;
}

.scale-enter-from,
.scale-leave-to {
  transform: scale(0.9);
}
</style>