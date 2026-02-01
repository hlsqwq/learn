<template>
  <div class="text-animate-wrapper" :class="`type-${type}`" ref="wrapperRef">
    <span v-for="(char, index) in textArray" :key="index" class="text-char"
      :style="{ animationDelay: `${index * 0.05}s` }">
      {{ char === ' ' ? '\u00A0' : char }}
    </span>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

const props = defineProps({
  text: {
    type: String,
    required: true
  },
  type: {
    type: String,
    default: 'rollIn',
    validator: (value) => {
      return ['rollIn', 'fadeIn', 'fadeInUp', 'whipIn', 'popIn', 'shiftInUp', 'whipInUp', 'calmInUp'].includes(value)
    }
  }
})

const wrapperRef = ref(null)
const isInView = ref(false)

const textArray = computed(() => {
  return props.text.split('')
})

const observer = new IntersectionObserver(
  (entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        isInView.value = true
        observer.unobserve(entry.target)
      }
    })
  },
  { threshold: 0.1 }
)

onMounted(() => {
  if (wrapperRef.value) {
    observer.observe(wrapperRef.value)
    // 页面加载时立即显示动画
    setTimeout(() => {
      isInView.value = true
    }, 100)
  }
})
</script>

<style scoped>
.text-animate-wrapper {
  display: inline-block;
  position: relative;
  z-index: 1;
}

.text-char {
  display: inline-block;
  opacity: 0;
  position: relative;
  z-index: 1;
}

/* Roll In */
.type-rollIn .text-char {
  animation: rollIn 0.6s ease-out forwards;
}

@keyframes rollIn {
  0% {
    opacity: 0;
    transform: translateX(-100px) rotate(-120deg);
  }

  100% {
    opacity: 1;
    transform: translateX(0) rotate(0deg);
  }
}

/* Fade In */
.type-fadeIn .text-char {
  animation: fadeIn 0.5s ease-out forwards;
}

@keyframes fadeIn {
  0% {
    opacity: 0;
  }

  100% {
    opacity: 1;
  }
}

/* Fade In Up */
.type-fadeInUp .text-char {
  animation: fadeInUp 0.6s ease-out forwards;
}

@keyframes fadeInUp {
  0% {
    opacity: 0;
    transform: translateY(30px);
  }

  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Whip In */
.type-whipIn .text-char {
  animation: whipIn 0.5s ease-out forwards;
}

@keyframes whipIn {
  0% {
    opacity: 0;
    transform: translateX(-100%) skewX(-15deg);
  }

  100% {
    opacity: 1;
    transform: translateX(0) skewX(0deg);
  }
}

/* Pop In */
.type-popIn .text-char {
  animation: popIn 0.5s cubic-bezier(0.68, -0.55, 0.265, 1.55) forwards;
}

@keyframes popIn {
  0% {
    opacity: 0;
    transform: scale(0);
  }

  50% {
    transform: scale(1.2);
  }

  100% {
    opacity: 1;
    transform: scale(1);
  }
}

/* Shift In Up */
.type-shiftInUp .text-char {
  animation: shiftInUp 0.6s ease-out forwards;
}

@keyframes shiftInUp {
  0% {
    opacity: 0;
    transform: translateY(50px) rotateX(90deg);
  }

  100% {
    opacity: 1;
    transform: translateY(0) rotateX(0deg);
  }
}

/* Whip In Up */
.type-whipInUp .text-char {
  animation: whipInUp 0.6s ease-out forwards;
}

@keyframes whipInUp {
  0% {
    opacity: 0;
    transform: translateY(100%) skewY(-10deg);
  }

  100% {
    opacity: 1;
    transform: translateY(0) skewY(0deg);
  }
}

/* Calm In Up */
.type-calmInUp .text-char {
  animation: calmInUp 0.8s ease-out forwards;
}

@keyframes calmInUp {
  0% {
    opacity: 0;
    transform: translateY(20px);
  }

  100% {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>