<template>
  <div class="marquee-container" :class="{ 'pause-on-hover': pauseOnHover, 'reverse': reverse, 'vertical': vertical }">
    <div class="marquee-content" :style="marqueeStyle" ref="marqueeContentRef">
      <div class="marquee-item" v-for="(item, index) in duplicatedItems" :key="index">
        <slot :item="item" :index="index">
          <span>{{ item }}</span>
        </slot>
      </div>
      <!-- 复制一份以实现无缝循环 -->
      <div class="marquee-item" v-for="(item, index) in duplicatedItems" :key="`duplicate-${index}`">
        <slot :item="item" :index="index">
          <span>{{ item }}</span>
        </slot>
      </div>
    </div>
  </div>
</template>

<script setup>
defineOptions({
  name: 'indeI'
})
import { computed, ref } from 'vue'

const props = defineProps({
  pauseOnHover: {
    type: Boolean,
    default: false
  },
  reverse: {
    type: Boolean,
    default: false
  },
  vertical: {
    type: Boolean,
    default: false
  },
  items: {
    type: Array,
    required: true
  },
  repeat: {
    type: Number,
    default: 1
  },
  speed: {
    type: Number,
    default: 50
  }
})

const marqueeContentRef = ref(null)

const duplicatedItems = computed(() => {
  const items = []
  for (let i = 0; i < props.repeat; i++) {
    items.push(...props.items)
  }
  return items
})

const marqueeStyle = computed(() => {
  return {
    animationDuration: `${props.speed}s`
  }
})

// ...existing code...
</script>

<style scoped>
.marquee-container {
  width: 100%;
  overflow-x: hidden;
  overflow-y: visible;
  position: relative;
  display: flex;
  align-items: center;
}

.marquee-content {
  display: flex;
  white-space: nowrap;
  animation: marquee linear infinite;
  width: fit-content;
  position: relative;
}

.marquee-container.vertical .marquee-content {
  flex-direction: column;
  animation: marquee-vertical linear infinite;
  width: 100%;
}

.marquee-container.reverse .marquee-content {
  animation: marquee-reverse linear infinite;
}

.marquee-container.vertical.reverse .marquee-content {
  animation: marquee-vertical-reverse linear infinite;
}

.marquee-container.pause-on-hover:hover .marquee-content {
  animation-play-state: paused;
}

.marquee-item {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  padding: 0 2rem;
}

.marquee-container.vertical .marquee-item {
  padding: 1rem 0;
}

@keyframes marquee {
  0% {
    transform: translateX(0);
  }

  100% {
    transform: translateX(-50%);
  }
}

@keyframes marquee-vertical {
  0% {
    transform: translateY(0);
  }

  100% {
    transform: translateY(-50%);
  }
}

@keyframes marquee-reverse {
  0% {
    transform: translateX(-50%);
  }

  100% {
    transform: translateX(0);
  }
}

@keyframes marquee-vertical-reverse {
  0% {
    transform: translateY(-50%);
  }

  100% {
    transform: translateY(0);
  }
}
</style>