<template>
  <button
    @click="$emit('click')"
    :class="[
      'w-full flex items-center px-4 py-2.5 rounded-xl transition-all duration-300 group',
      active ? 'bg-white shadow-sm text-rose-500' : 'text-gray-500 hover:bg-white/60 hover:text-black'
    ]"
  >
    <div :class="['flex-shrink-0 transition-transform duration-300', active ? 'scale-110 text-rose-500' : 'group-hover:scale-110']">
      <component :is="iconComponent" :size="18" />
    </div>
    <span :class="[
      'ml-3 text-xs tracking-tight truncate transition-colors',
      active ? 'font-black text-rose-500' : 'font-bold text-gray-600 group-hover:text-black'
    ]">
      {{ label }}
    </span>
  </button>
</template>

<script setup>
import { computed } from 'vue'
import {
  LayoutGrid,
  Trophy,
  Heart,
  History,
  Music,
  User,
  ChevronRight
} from 'lucide-vue-next'

const props = defineProps({
  label: String,
  iconName: String,
  active: Boolean
})

defineEmits(['click'])

const iconMap = {
  'layout-grid': LayoutGrid,
  'trophy': Trophy,
  'heart': Heart,
  'history': History,
  'music': Music,
  'user': User,
  'chevron-right': ChevronRight
}

const iconComponent = computed(() => iconMap[props.iconName] || Heart)
</script>