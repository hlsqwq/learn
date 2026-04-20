<template>
  <section class="browse-page">
    <!-- 页面标题 -->
    <div class="flex items-end justify-between mb-8 border-b border-gray-200/50 pb-4">
      <h1 class="text-4xl font-black tracking-tight">新发现</h1>
    </div>

    <!-- 焦点大图轮播和排行榜 -->
    <div class="flex flex-col lg:flex-row gap-8 mb-12">
      <!-- 焦点轮播 -->
      <div class="flex-1 grid grid-cols-1 md:grid-cols-2 gap-4">
        <div v-for="(item, index) in featureData.slice(0, 2)" :key="item.id"
          @click="$emit('play-song', { name: item.title, artist: '推荐合集', cover: item.image })"
          class="feature-card group relative aspect-video rounded-2xl overflow-hidden shadow-xl cursor-pointer transition-all hover:-translate-y-1">
          <img :src="item.image" :alt="item.title"
            class="absolute inset-0 w-full h-full object-cover group-hover:scale-105 transition-transform duration-700" />
          <div
            class="absolute inset-0 bg-gradient-to-t from-black/70 via-black/10 to-transparent p-6 flex flex-col justify-end">
            <p class="text-white/70 text-[10px] font-black uppercase tracking-widest mb-1">{{ item.subtitle }}</p>
            <h3 class="text-white text-lg font-black leading-tight">{{ item.title }}</h3>
          </div>
        </div>
      </div>

      <!-- 排行榜侧边栏 -->
      <div
        class="w-full lg:w-80 bg-white/60 backdrop-blur-sm rounded-3xl p-6 border border-white shadow-sm flex flex-col">
        <div class="flex items-center justify-between mb-6">
          <h2 class="text-sm font-black uppercase tracking-tighter flex items-center gap-2">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4 text-amber-500" fill="none" viewBox="0 0 24 24"
              stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M5 3v4M3 5h4M6 17v4m-2-2h4m5-16l2.286 6.857L21 12l-5.714 2.143L13 21l-2.286-6.857L5 12l5.714-2.143L13 3z" />
            </svg>
            歌手排行榜
          </h2>
          <span class="text-[10px] font-bold text-rose-500 cursor-pointer hover:underline">全部 &gt;</span>
        </div>
        <div class="space-y-4 flex-1">
          <div v-for="(item, index) in rankingData" :key="item.id"
            @click="$emit('play-song', { name: `${item.name} 热门单曲`, artist: item.name })"
            class="ranking-item group cursor-pointer hover:bg-white/40 p-2 -mx-2 rounded-xl transition-all">
            <span
              class="rank-number text-xl font-black italic text-gray-300 group-hover:text-rose-500 transition-colors w-8">{{
              item.rank }}</span>
            <div class="flex-1 ml-2">
              <p class="text-sm font-black tracking-tight">{{ item.name }}</p>
              <p class="text-[9px] text-gray-400 font-bold uppercase tracking-widest">热度上升</p>
            </div>
            <svg xmlns="http://www.w3.org/2000/svg"
              class="w-4 h-4 text-rose-500 opacity-0 group-hover:opacity-100 transition-all" fill="currentColor"
              viewBox="0 0 24 24">
              <path d="M8 5v14l11-7z" />
            </svg>
          </div>
        </div>
      </div>
    </div>

    <!-- 心情与场景 -->
    <div class="mb-12">
      <h2 class="text-lg font-black mb-4">心情与场景</h2>
      <div class="flex space-x-4 overflow-x-auto custom-scrollbar pb-4">
        <button v-for="(mood, i) in moods" :key="i"
          @click="$emit('play-song', { name: `${mood.label}电台`, artist: 'YXMusic' })"
          :class="`mood-button ${mood.color}`">
          <span class="text-lg">{{ mood.icon }}</span>
          <span class="font-bold text-sm">{{ mood.label }}</span>
        </button>
      </div>
    </div>

    <!-- 全新发行 -->
    <div class="mb-16">
      <h2 class="text-lg font-black mb-6">全新发行</h2>
      <div class="grid grid-cols-2 md:grid-cols-4 gap-6">
        <div v-for="album in newReleases" :key="album.id" class="group cursor-pointer"
          @click="$emit('play-song', { name: album.title, artist: album.artist, cover: album.cover })">
          <div class="album-cover-container aspect-square rounded-2xl overflow-hidden mb-3 shadow-md relative">
            <img :src="album.cover"
              class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500" alt="" />
            <div
              class="album-overlay absolute inset-0 bg-black/20 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center">
              <div
                class="play-button-overlay w-10 h-10 bg-white/90 rounded-full flex items-center justify-center shadow-lg">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 text-black ml-0.5" fill="currentColor"
                  viewBox="0 0 24 24">
                  <path d="M8 5v14l11-7z" />
                </svg>
              </div>
            </div>
          </div>
          <h4 class="font-black text-sm truncate">{{ album.title }}</h4>
          <p class="text-[11px] text-gray-500 font-bold truncate mt-0.5">{{ album.artist }}</p>
        </div>
      </div>
    </div>

    <!-- 独家 MV -->
    <div class="mb-14">
      <h2 class="text-lg font-black mb-6">独家 MV</h2>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
        <div v-for="mv in mvRecommendations" :key="mv.id" class="group cursor-pointer"
          @click="$emit('play-song', { name: mv.title, artist: mv.artist, cover: mv.cover })">
          <div class="mv-container aspect-[16/9] rounded-2xl overflow-hidden shadow-lg relative bg-black">
            <img :src="mv.cover"
              class="w-full h-full object-cover opacity-90 group-hover:scale-105 transition-transform duration-700"
              alt="" />
            <div class="mv-play-button absolute inset-0 flex items-center justify-center">
              <div
                class="play-circle w-12 h-12 bg-white/20 backdrop-blur-md rounded-full border border-white/30 flex items-center justify-center group-hover:scale-110 transition-transform">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6 text-white ml-0.5" fill="currentColor"
                  viewBox="0 0 24 24">
                  <path d="M8 5v14l11-7z" />
                </svg>
              </div>
            </div>
            <div
              class="mv-badge absolute bottom-3 left-3 bg-black/50 backdrop-blur-md px-2 py-1 rounded text-[9px] text-white font-bold tracking-widest uppercase">
              4K 视频</div>
          </div>
          <h4 class="mt-4 font-black text-base">{{ mv.title }}</h4>
          <p class="text-gray-500 text-xs font-bold">{{ mv.artist }}</p>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { api, searchAPI, contentAPI } from '../services/api'

defineEmits(['play-song'])

const loading = ref(true)
const captchaResult = ref(null)
const featureData = ref([])
const rankingData = ref([])
const newReleases = ref([])
const mvRecommendations = ref([])
const moods = ref([])

onMounted(async () => {
  try {
    const res = await api.createCaptcha()
    captchaResult.value = res
    console.log('Captcha API test result:', res)
  } catch (error) {
    console.error('Failed to fetch captcha:', error)
  }
  await fetchData()
})

const fetchData = async () => {
  loading.value = true
  try {
    const [topSingers, songsData, categoriesData] = await Promise.all([
      contentAPI.getTopSingers(),
      contentAPI.getSongPage(1, { num: 1, size: 8 }),
      contentAPI.getCategoryList()
    ])

    if (topSingers.code === 200) {
      rankingData.value = (topSingers.data || []).slice(0, 5).map((s, i) => ({
        id: s.id,
        name: s.name,
        rank: String(i + 1).padStart(2, '0'),
        avatar: s.avatar
      }))
    }

    if (songsData.code === 200) {
      newReleases.value = (songsData.data?.item || []).map(s => ({
        id: s.id,
        title: s.name,
        artist: s.album || '未知歌手',
        cover: s.avatar,
        duration: s.duration
      }))
    }

    if (categoriesData.code === 200) {
      const moodIcons = ['☕', '⚡', '🌙', '😊', '🎸', '🎹', '🎤', '💃']
      const moodColors = ['bg-blue-100 text-blue-600', 'bg-amber-100 text-amber-600', 'bg-indigo-100 text-indigo-600', 'bg-rose-100 text-rose-600', 'bg-purple-100 text-purple-600', 'bg-green-100 text-green-600', 'bg-pink-100 text-pink-600', 'bg-orange-100 text-orange-600']
      moods.value = (categoriesData.data || []).slice(0, 8).map((c, i) => ({
        label: c.content,
        icon: moodIcons[i % moodIcons.length],
        color: moodColors[i % moodColors.length],
        id: c.id
      }))
    }

    featureData.value = [
      { id: 1, title: "新歌推荐", subtitle: "每日更新", image: "https://images.unsplash.com/photo-1493225255756-d9584f8606e9?auto=format&fit=crop&w=800&q=80" },
      { id: 2, title: "热门榜单", subtitle: "实时更新", image: "https://images.unsplash.com/photo-1514525253361-bee8718a7439?auto=format&fit=crop&w=800&q=80" },
    ]

    try {
      const mvData = await contentAPI.getMvList()
      if (mvData.code === 200) {
        mvRecommendations.value = (mvData.data || []).slice(0, 4).map(m => ({
          id: m.id,
          title: m.name,
          artist: 'MV',
          cover: m.avatar
        }))
      }
    } catch (e) {
      console.error('Failed to fetch MV list:', e)
    }

  } catch (error) {
    console.error('Failed to fetch discovery data:', error)
  } finally {
    loading.value = false
  }
}

// 焦点轮播数据
// const featureData = ref([
//   { id: 1, title: "A-List: 国际流行", subtitle: "YXMusic 必听", image: "https://images.unsplash.com/photo-1493225255756-d9584f8606e9?auto=format&fit=crop&w=800&q=80" },
//   { id: 2, title: "今日最热排行", subtitle: "实时更新", image: "https://images.unsplash.com/photo-1514525253361-bee8718a7439?auto=format&fit=crop&w=800&q=80" },
//   { id: 3, title: "华语金曲精选", subtitle: "时光留声机", image: "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?auto=format&fit=crop&w=800&q=80" },
// ])

// 排行榜数据
// const rankingData = ref([
//   { id: 1, name: "周杰伦", rank: "01", trend: "up" },
//   { id: 2, name: "陈奕迅", rank: "02", trend: "stable" },
//   { id: 3, name: "Taylor Swift", rank: "03", trend: "up" },
// ])

// 全新发行
// const newReleases = ref([
//   { id: 1, title: "午夜梦回", artist: "星野", cover: "https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17?auto=format&fit=crop&w=300&q=80" },
//   { id: 2, title: "夏日微风", artist: "橘子海", cover: "https://images.unsplash.com/photo-1459749411175-04bf5292ceea?auto=format&fit=crop&w=300&q=80" },
//   { id: 3, title: "Lost In City", artist: "NEO", cover: "https://images.unsplash.com/photo-1498038432885-c6f3f1b912ee?auto=format&fit=crop&w=300&q=80" },
//   { id: 4, title: "1989 (TV)", artist: "Taylor Swift", cover: "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?auto=format&fit=crop&w=300&q=80" },
// ])

// MV 推荐
// const mvRecommendations = ref([
//   { id: 1, title: "告白气球", artist: "周杰伦", cover: "https://images.unsplash.com/photo-1498038432885-c6f3f1b912ee?w=800&h=450&fit=crop" },
//   { id: 2, title: "反方向的钟", artist: "周杰伦", cover: "https://images.unsplash.com/photo-1496293455970-f8581aae0e3c?w=800&h=450&fit=crop" },
// ])

// 心情分类
// const moods = ref([
//   { label: "轻松", icon: "☕", color: "bg-blue-100 text-blue-600" },
//   { label: "专注", icon: "⚡", color: "bg-amber-100 text-amber-600" },
//   { label: "助眠", icon: "🌙", color: "bg-indigo-100 text-indigo-600" },
//   { label: "愉悦", icon: "😊", color: "bg-rose-100 text-rose-600" }
// ])
</script>

<style scoped>
.browse-page {
  min-height: 60vh;
}

.feature-card {
  cursor: pointer;
}

.ranking-item {
  display: flex;
  align-items: center;
}

.rank-number {
  flex-shrink: 0;
}

.mood-button {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  border-radius: 16px;
  transition: all;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  white-space: nowrap;
  background: none;
  border: none;
  cursor: pointer;
  font-family: inherit;
}

.mood-button:hover {
  filter: brightness(0.95);
}

.album-cover-container {
  position: relative;
}

.album-overlay {
  display: flex;
  align-items: center;
  justify-content: center;
}

.play-button-overlay {
  display: flex;
  align-items: center;
  justify-content: center;
}

.mv-container {
  position: relative;
}

.mv-play-button {
  display: flex;
  align-items: center;
  justify-content: center;
}

.play-circle {
  display: flex;
  align-items: center;
  justify-content: center;
}

.mv-badge {
  backdrop-filter: blur(8px);
}

/* 自定义滚动条 */
.custom-scrollbar::-webkit-scrollbar {
  width: 4px;
  height: 4px;
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  background: rgba(0, 0, 0, 0.1);
  border-radius: 10px;
}

.custom-scrollbar:hover::-webkit-scrollbar-thumb {
  background: rgba(0, 0, 0, 0.2);
}
</style>
