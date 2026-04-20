<template>
  <div
    @click="$emit('click')"
    :class="[
      'relative flex items-center justify-center w-20 h-14 rounded-2xl cursor-pointer transition-all duration-300 border border-transparent group overflow-hidden flex-shrink-0',
      active ? 'bg-white shadow-md text-rose-500 border-white' : 'bg-gray-200/40 text-gray-500 hover:bg-white hover:shadow-sm hover:text-black'
    ]"
  >
    <div class="relative flex items-center justify-center w-full h-full">
      <div :class="[
        'absolute flex items-center justify-center transition-all duration-300',
        active ? 'opacity-0 scale-75 -translate-y-2' : 'opacity-100 group-hover:opacity-0 group-hover:scale-75 group-hover:-translate-y-2'
      ]">
        <component :is="iconComponent" :size="20" />
      </div>
      <span :class="[
        'text-[11px] font-black transition-all duration-300',
        active ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-2 group-hover:opacity-100 group-hover:translate-y-0'
      ]">
        {{ label }}
      </span>
    </div>
  </div>
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

const iconComponent = computed(() => iconMap[props.iconName] || LayoutGrid)
</script>