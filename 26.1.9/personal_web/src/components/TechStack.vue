<template>
  <div class="tech-stack-section" ref="sectionRef">
    <div class="tech-stack-wrapper">
      <!-- 第一行：向左滚动 -->
      <div class="marquee-row">
        <Marquee :items="techStackRow1" :repeat="3" :speed="60" :reverse="false" class="tech-marquee">
          <template #default="{ item }">
            <div class="tech-item">
              <span class="tech-name">{{ item }}</span>
            </div>
          </template>
        </Marquee>
      </div>

      <!-- 第二行：向右滚动 -->
      <div class="marquee-row">
        <Marquee :items="techStackRow2" :repeat="3" :speed="60" :reverse="true" class="tech-marquee">
          <template #default="{ item }">
            <div class="tech-item">
              <span class="tech-name">{{ item }}</span>
            </div>
          </template>
        </Marquee>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import Marquee from './Marquee.vue'

const techStack = [
  'Java',
  'SpringBoot',
  'MySQL',
  'C/C++',
  'Vue',
  'SpringCloud',
  'Python',
  'HTML',
  'CSS',
  'JavaScript',
  'Redis',
  'MinIO',
  'Nacos'
]

// 分成两行
const techStackRow1 = techStack.slice(0, Math.ceil(techStack.length / 2))
const techStackRow2 = techStack.slice(Math.ceil(techStack.length / 2))

const sectionRef = ref(null)
const isVisible = ref(false)

const observer = new IntersectionObserver(
  (entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        isVisible.value = true
      }
    })
  },
  { threshold: 0.1 }
)

onMounted(() => {
  if (sectionRef.value) {
    observer.observe(sectionRef.value)
  }
})

onUnmounted(() => {
  if (observer) {
    observer.disconnect()
  }
})
</script>

<style scoped>
.tech-stack-section {
  width: 100%;
  box-sizing: border-box;
  height: 100vh;
  min-height: 450px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(180deg,
      rgba(102, 126, 234, 0.15) 0%,
      rgba(118, 75, 162, 0.2) 20%,
      rgba(240, 147, 251, 0.15) 40%,
      rgba(245, 87, 108, 0.12) 60%,
      rgba(79, 172, 254, 0.1) 80%,
      rgba(10, 10, 10, 0.4) 100%);
  padding: 8rem 0 7rem;
  position: relative;
  overflow: hidden;
  margin: 0;
}

.tech-stack-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent 0%, rgba(255, 255, 255, 0.03) 50%, transparent 100%);
  animation: shimmer 4s infinite;
  animation-play-state: running;
}

@media (prefers-reduced-motion: no-preference) {
  .tech-stack-section::before {
    animation-play-state: running;
  }
}

@keyframes shimmer {
  0% {
    transform: translateX(-100%);
  }

  100% {
    transform: translateX(100%);
  }
}

.tech-stack-wrapper {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 3rem;
  padding: 0 2rem;
  overflow-x: hidden;
}

.marquee-row {
  width: 100vw;
  max-width: 100vw;
  box-sizing: border-box;
  overflow-x: hidden;
  overflow-y: visible;
  position: relative;
  padding: 3rem 0;
  margin: 1.5rem 0;
  left: 50%;
  transform: translateX(-50%);
}

.tech-marquee {
  width: 100%;
  overflow-x: hidden;
}

.tech-item {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 1.2rem 3.5rem;
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: 50px;
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
  margin: 0 1.5rem;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  position: relative;
  z-index: 1;
}

.tech-item:hover {
  background: rgba(255, 255, 255, 0.15);
  border-color: rgba(255, 255, 255, 0.3);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.3);
}

.tech-name {
  font-size: 1.6rem;
  font-weight: 500;
  color: #fff;
  letter-spacing: 0.1em;
  white-space: nowrap;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
}

@media (max-width: 768px) {
  .tech-stack-section {
    padding: 5rem 0 4rem;
    min-height: 350px;
  }

  .tech-stack-wrapper {
    gap: 2rem;
    padding: 0 1rem;
  }

  .marquee-row {
    padding: 2rem 0;
    margin: 1rem 0;
  }

  .tech-item {
    padding: 1rem 2.5rem;
    margin: 0 1rem;
  }

  .tech-name {
    font-size: 1.3rem;
  }
}
</style>