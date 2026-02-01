<template>
  <div class="timeline-container" ref="containerRef">
    <div class="timeline-header" :class="{ 'visible': isVisible }">
      <h1 class="timeline-title">时间线</h1>
      <p class="timeline-subtitle">记录成长与思考的每一个瞬间</p>
    </div>

    <div class="timeline-content" :class="{ 'visible': isVisible }">
      <swiper :modules="modules" :slides-per-view="1" :space-between="30" :speed="800" :navigation="{
        nextEl: navigationNextRef,
        prevEl: navigationPrevRef,
      }" :breakpoints="{
        768: {
          slidesPerView: 2,
          spaceBetween: 40,
        },
        1024: {
          slidesPerView: 3,
          spaceBetween: 50,
        }
      }" class="timeline-swiper">
        <swiper-slide v-for="(item, index) in timelineItems" :key="index" class="timeline-slide">
          <div class="timeline-card">
            <div class="timeline-date">
              <span class="date-day">{{ item.day }}</span>
              <span class="date-month">{{ item.month }}</span>
            </div>
            <div class="timeline-content-wrapper">
              <h3 class="timeline-item-title">{{ item.title }}</h3>
              <p class="timeline-item-description">{{ item.description }}</p>
              <div class="timeline-tag" v-if="item.tag">
                <span>{{ item.tag }}</span>
              </div>
            </div>
          </div>
        </swiper-slide>
      </swiper>

      <div class="swiper-navigation-custom">
        <button :ref="el => navigationPrevRef = el" class="swiper-button-prev">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M19 12H5M12 19l-7-7 7-7" />
          </svg>
        </button>
        <button :ref="el => navigationNextRef = el" class="swiper-button-next">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M5 12h14M12 5l7 7-7 7" />
          </svg>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Swiper, SwiperSlide } from 'swiper/vue'
import { Navigation } from 'swiper/modules'
import 'swiper/css'
import 'swiper/css/navigation'

const modules = [Navigation]

const navigationPrevRef = ref(null)
const navigationNextRef = ref(null)
const containerRef = ref(null)
const isVisible = ref(false)

const observer = new IntersectionObserver(
  (entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        isVisible.value = true
        observer.unobserve(entry.target)
      }
    })
  },
  { threshold: 0.1 }
)

onMounted(() => {
  if (containerRef.value) {
    observer.observe(containerRef.value)
  }
})

const timelineItems = ref([
  {
    day: '24',
    month: '八月',
    title: '项目启动',
    description: '开始构建个人网站，记录学习与成长的历程，分享技术见解和生活感悟。',
    tag: 'news'
  },
  {
    day: '25',
    month: '八月',
    title: '技术探索',
    description: '深入研究Vue3的Composition API，探索现代前端开发的最佳实践。',
    tag: 'tech'
  },
  {
    day: '26',
    month: '八月',
    title: '设计思考',
    description: '思考如何将简约与设计感结合，创造令人眼前一亮的用户体验。',
    tag: 'design'
  },
  {
    day: '27',
    month: '八月',
    title: '持续优化',
    description: '不断优化代码结构，提升性能，追求更优雅的实现方式。',
    tag: 'optimize'
  },
  {
    day: '28',
    month: '八月',
    title: '分享交流',
    description: '与社区分享开发经验，从交流中学习，在反馈中成长。',
    tag: 'share'
  },
  {
    day: '29',
    month: '八月',
    title: '未来规划',
    description: '规划下一步的学习方向，保持对新技术的好奇心和探索精神。',
    tag: 'future'
  }
])
</script>

<style scoped>
.timeline-container {
  height: 100vh;
  overflow: hidden;
  width: 100%;
  min-height: 100vh;
  background: linear-gradient(180deg,
      rgba(10, 10, 10, 0.4) 0%,
      rgba(20, 20, 30, 0.5) 20%,
      rgba(26, 26, 36, 0.6) 50%,
      rgba(26, 26, 26, 0.7) 80%,
      rgba(10, 10, 10, 0.9) 100%);
  padding: 4rem 2rem 3rem;
  color: #fff;
  margin: 0;
  position: relative;
}

/* 顶部渐变过渡从技术栈部分 */
.timeline-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 20%;
  background: linear-gradient(180deg,
      rgba(79, 172, 254, 0.1) 0%,
      rgba(245, 87, 108, 0.08) 30%,
      rgba(10, 10, 10, 0.4) 100%);
  pointer-events: none;
}

.timeline-header {
  text-align: center;
  margin-bottom: 2rem;
  opacity: 0;
  transform: translateY(-30px);
  transition: opacity 1s ease-out, transform 1s ease-out;
}

.timeline-header.visible {
  opacity: 1;
  transform: translateY(0);
}

.timeline-title {
  font-size: 3.5rem;
  font-weight: 300;
  margin: 0 0 1rem 0;
  letter-spacing: 0.2em;
  background: linear-gradient(135deg, #fff 0%, rgba(255, 255, 255, 0.6) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.timeline-subtitle {
  font-size: 1.2rem;
  opacity: 0.7;
  font-weight: 300;
  letter-spacing: 0.1em;
}

.timeline-content {
  max-width: 1400px;
  margin: 0 auto;
  position: relative;
  opacity: 0;
  transform: translateY(30px);
  transition: opacity 1s ease-out 0.3s, transform 1s ease-out 0.3s;
}

.timeline-content.visible {
  opacity: 1;
  transform: translateY(0);
}

.timeline-swiper {
  padding: 2rem 0 5rem;
  overflow: visible;
}

.timeline-slide {
  height: auto;
}

.timeline-card {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: 20px;
  padding: 3rem;
  height: 100%;
  transition: all 0.4s ease;
  backdrop-filter: blur(10px);
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  position: relative;
  overflow: visible;
  margin: 1rem 0;
}

.timeline-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.05) 0%, transparent 100%);
  opacity: 0;
  transition: opacity 0.4s ease;
}

.timeline-card:hover {
  transform: translateY(-15px);
  border-color: rgba(255, 255, 255, 0.3);
  box-shadow: 0 25px 70px rgba(0, 0, 0, 0.6);
  z-index: 10;
}

.timeline-card:hover::before {
  opacity: 1;
}

.timeline-date {
  display: flex;
  align-items: baseline;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
}

.date-day {
  font-size: 3rem;
  font-weight: 300;
  line-height: 1;
  color: #fff;
}

.date-month {
  font-size: 1.2rem;
  opacity: 0.7;
  font-weight: 300;
  letter-spacing: 0.1em;
}

.timeline-content-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.timeline-item-title {
  font-size: 1.5rem;
  font-weight: 500;
  margin: 0;
  letter-spacing: 0.05em;
  color: #fff;
}

.timeline-item-description {
  font-size: 1rem;
  line-height: 1.8;
  opacity: 0.8;
  margin: 0;
  flex: 1;
  font-weight: 300;
}

.timeline-tag {
  margin-top: auto;
}

.timeline-tag span {
  display: inline-block;
  padding: 0.4rem 1rem;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 20px;
  font-size: 0.85rem;
  letter-spacing: 0.1em;
  text-transform: uppercase;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.swiper-navigation-custom {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-top: 2rem;
}

.swiper-button-prev,
.swiper-button-next {
  width: 50px;
  height: 50px;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  color: #fff;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  position: relative;
  margin: 0;
}

.swiper-button-prev:hover,
.swiper-button-next:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: scale(1.1);
  border-color: rgba(255, 255, 255, 0.4);
}

.swiper-button-prev::after,
.swiper-button-next::after {
  display: none;
}

@media (max-width: 768px) {
  .timeline-container {
    padding: 2rem 1.5rem 1.5rem;
  }

  .timeline-title {
    font-size: 2.5rem;
  }

  .timeline-card {
    padding: 2rem;
  }

  .date-day {
    font-size: 2.5rem;
  }
}
</style>