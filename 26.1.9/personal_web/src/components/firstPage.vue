<template>
  <div class="home-container" ref="homeRef">
    <div class="gradient-background" :class="{ 'animate': isVisible }"></div>
    <div class="home-content" :class="{ 'visible': isVisible }">
      <TextAnimate text="欢迎来到" type="rollIn" class="welcome-text" />
      <div class="name-wrapper">
        <TextAnimate text="yixiao" type="rollIn" class="name-text" />
        <div class="orbit-icons">
          <div class="orbit-icon avatar-icon">
            <img src="../assets/a3.png" alt="头像" />
          </div>
          <div class="orbit-icon edge-icon">
            <img src="../assets/p2.png" alt="Edge" />
          </div>
          <div class="orbit-icon vscode-icon">
            <img src="../assets/code_70x70.png" alt="VSCode" />
          </div>
          <div class="orbit-icon idea-icon">
            <img src="../assets/p1.png" alt="IDEA" />
          </div>
        </div>
      </div>
      <TextAnimate text="的个人网站" type="rollIn" class="welcome-text" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import TextAnimate from '../components/TextAnimate.vue'

const homeRef = ref(null)
const isVisible = ref(false)
let timeoutId = null

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
  if (homeRef.value) {
    observer.observe(homeRef.value)
    // 页面加载时立即显示
    timeoutId = setTimeout(() => {
      isVisible.value = true
    }, 100)
  }
})

onUnmounted(() => {
  if (timeoutId) {
    clearTimeout(timeoutId)
  }
  if (observer) {
    observer.disconnect()
  }
})
</script>

<style scoped>
.home-container {
  width: 100%;
  min-height: 100vh;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: visible;
}

.gradient-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg,
      #667eea 0%,
      #764ba2 20%,
      #8b7fb8 40%,
      #f093fb 50%,
      #f5576c 70%,
      #4facfe 100%);
  background-size: 400% 400%;
  opacity: 0;
  transition: opacity 1s ease-in-out;
}

/* 底部渐变过渡到技术栈部分 */
.gradient-background::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 30%;
  background: linear-gradient(180deg,
      transparent 0%,
      rgba(102, 126, 234, 0.2) 50%,
      rgba(118, 75, 162, 0.25) 100%);
  pointer-events: none;
}

.gradient-background.animate {
  opacity: 1;
  animation: gradientShift 15s ease infinite;
  animation-play-state: running;
}

@media (prefers-reduced-motion: no-preference) {
  .gradient-background.animate {
    animation-play-state: running;
  }
}

@keyframes gradientShift {
  0% {
    background-position: 0% 50%;
  }

  50% {
    background-position: 100% 50%;
  }

  100% {
    background-position: 0% 50%;
  }
}

.home-content {
  position: relative;
  z-index: 10;
  text-align: center;
  color: #fff;
  opacity: 0;
  transform: translateY(30px);
  transition: opacity 1s ease-out, transform 1s ease-out;
}

.home-content.visible {
  opacity: 1;
  transform: translateY(0);
}

.welcome-text {
  font-size: 3rem;
  font-weight: 300;
  margin: 0.5rem 0;
  letter-spacing: 0.2em;
  display: block;
}

.name-text {
  font-size: 6rem;
  font-weight: 400;
  margin: 1rem 0;
  letter-spacing: 0.1em;
  display: block;
  color: #fff;
  text-shadow: 0 0 30px rgba(255, 255, 255, 0.5), 0 0 60px rgba(255, 255, 255, 0.3);
  position: relative;
  z-index: 10;
}

@media (max-width: 768px) {
  .welcome-text {
    font-size: 2rem;
  }

  .name-text {
    font-size: 4rem;
  }
}

@media (max-width: 480px) {
  .welcome-text {
    font-size: 1.5rem;
  }

  .name-text {
    font-size: 3rem;
  }
}

.name-wrapper {
  position: relative;
  display: inline-block;
  perspective: 1000px;
}

.orbit-icons {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 200%;
  height: 200%;
  pointer-events: none;
}

.orbit-icon {
  position: absolute;
  width: 50px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 50%;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  overflow: hidden;
}

.orbit-icon img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-icon {
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation: iconOrbit1 9s linear infinite;
}

.edge-icon {
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation: iconOrbit2 13s linear infinite;
  animation-delay: -2.25s;
}

.vscode-icon {
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation: iconOrbit3 15s linear infinite;
  animation-delay: -4.5s;
}

.idea-icon {
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation: iconOrbit4 11s linear infinite;
  animation-delay: -6.75s;
}

@keyframes iconOrbit1 {
  0% {
    transform: translate(-50%, -50%) translateX(0px) translateY(180px) translateZ(300px) scale(1.3);
    opacity: 1;
    z-index: 100;
  }

  6.25% {
    transform: translate(-50%, -50%) translateX(117px) translateY(159px) translateZ(243px) scale(1.25);
    opacity: 1;
    z-index: 87.5;
  }

  12.5% {
    transform: translate(-50%, -50%) translateX(212px) translateY(127px) translateZ(212px) scale(1.15);
    opacity: 1;
    z-index: 75;
  }

  18.75% {
    transform: translate(-50%, -50%) translateX(272px) translateY(70px) translateZ(117px) scale(1.075);
    opacity: 1;
    z-index: 62.5;
  }

  25% {
    transform: translate(-50%, -50%) translateX(300px) translateY(0px) translateZ(0px) scale(1);
    opacity: 1;
    z-index: 50;
  }

  31.25% {
    transform: translate(-50%, -50%) translateX(272px) translateY(-70px) translateZ(-117px) scale(0.925);
    opacity: 0.875;
    z-index: 37.5;
  }

  37.5% {
    transform: translate(-50%, -50%) translateX(212px) translateY(-127px) translateZ(-212px) scale(0.85);
    opacity: 0.75;
    z-index: 25;
  }

  43.75% {
    transform: translate(-50%, -50%) translateX(117px) translateY(-159px) translateZ(-243px) scale(0.775);
    opacity: 0.625;
    z-index: 12.5;
  }

  50% {
    transform: translate(-50%, -50%) translateX(0px) translateY(-180px) translateZ(-300px) scale(0.7);
    opacity: 0.5;
    z-index: 0;
  }

  56.25% {
    transform: translate(-50%, -50%) translateX(-117px) translateY(-159px) translateZ(-243px) scale(0.775);
    opacity: 0.625;
    z-index: 12.5;
  }

  62.5% {
    transform: translate(-50%, -50%) translateX(-212px) translateY(-127px) translateZ(-212px) scale(0.85);
    opacity: 0.75;
    z-index: 25;
  }

  68.75% {
    transform: translate(-50%, -50%) translateX(-272px) translateY(-70px) translateZ(-117px) scale(0.925);
    opacity: 0.875;
    z-index: 37.5;
  }

  75% {
    transform: translate(-50%, -50%) translateX(-300px) translateY(0px) translateZ(0px) scale(1);
    opacity: 1;
    z-index: 50;
  }

  81.25% {
    transform: translate(-50%, -50%) translateX(-272px) translateY(70px) translateZ(117px) scale(1.075);
    opacity: 1;
    z-index: 62.5;
  }

  87.5% {
    transform: translate(-50%, -50%) translateX(-212px) translateY(127px) translateZ(212px) scale(1.15);
    opacity: 1;
    z-index: 75;
  }

  93.75% {
    transform: translate(-50%, -50%) translateX(-117px) translateY(159px) translateZ(243px) scale(1.25);
    opacity: 1;
    z-index: 87.5;
  }

  100% {
    transform: translate(-50%, -50%) translateX(0px) translateY(180px) translateZ(300px) scale(1.3);
    opacity: 1;
    z-index: 100;
  }
}

@keyframes iconOrbit2 {
  0% {
    transform: translate(-50%, -50%) translateX(0px) translateY(180px) translateZ(300px) scale(1.3);
    opacity: 1;
    z-index: 100;
  }

  6.25% {
    transform: translate(-50%, -50%) translateX(117px) translateY(159px) translateZ(243px) scale(1.25);
    opacity: 1;
    z-index: 87.5;
  }

  12.5% {
    transform: translate(-50%, -50%) translateX(212px) translateY(127px) translateZ(212px) scale(1.15);
    opacity: 1;
    z-index: 75;
  }

  18.75% {
    transform: translate(-50%, -50%) translateX(272px) translateY(70px) translateZ(117px) scale(1.075);
    opacity: 1;
    z-index: 62.5;
  }

  25% {
    transform: translate(-50%, -50%) translateX(300px) translateY(0px) translateZ(0px) scale(1);
    opacity: 1;
    z-index: 50;
  }

  31.25% {
    transform: translate(-50%, -50%) translateX(272px) translateY(-70px) translateZ(-117px) scale(0.925);
    opacity: 0.875;
    z-index: 37.5;
  }

  37.5% {
    transform: translate(-50%, -50%) translateX(212px) translateY(-127px) translateZ(-212px) scale(0.85);
    opacity: 0.75;
    z-index: 25;
  }

  43.75% {
    transform: translate(-50%, -50%) translateX(117px) translateY(-159px) translateZ(-243px) scale(0.775);
    opacity: 0.625;
    z-index: 12.5;
  }

  50% {
    transform: translate(-50%, -50%) translateX(0px) translateY(-180px) translateZ(-300px) scale(0.7);
    opacity: 0.5;
    z-index: 0;
  }

  56.25% {
    transform: translate(-50%, -50%) translateX(-117px) translateY(-159px) translateZ(-243px) scale(0.775);
    opacity: 0.625;
    z-index: 12.5;
  }

  62.5% {
    transform: translate(-50%, -50%) translateX(-212px) translateY(-127px) translateZ(-212px) scale(0.85);
    opacity: 0.75;
    z-index: 25;
  }

  68.75% {
    transform: translate(-50%, -50%) translateX(-272px) translateY(-70px) translateZ(-117px) scale(0.925);
    opacity: 0.875;
    z-index: 37.5;
  }

  75% {
    transform: translate(-50%, -50%) translateX(-300px) translateY(0px) translateZ(0px) scale(1);
    opacity: 1;
    z-index: 50;
  }

  81.25% {
    transform: translate(-50%, -50%) translateX(-272px) translateY(70px) translateZ(117px) scale(1.075);
    opacity: 1;
    z-index: 62.5;
  }

  87.5% {
    transform: translate(-50%, -50%) translateX(-212px) translateY(127px) translateZ(212px) scale(1.15);
    opacity: 1;
    z-index: 75;
  }

  93.75% {
    transform: translate(-50%, -50%) translateX(-117px) translateY(159px) translateZ(243px) scale(1.25);
    opacity: 1;
    z-index: 87.5;
  }

  100% {
    transform: translate(-50%, -50%) translateX(0px) translateY(180px) translateZ(300px) scale(1.3);
    opacity: 1;
    z-index: 100;
  }
}

@keyframes iconOrbit3 {
  0% {
    transform: translate(-50%, -50%) translateX(0px) translateY(180px) translateZ(300px) scale(1.3);
    opacity: 1;
    z-index: 100;
  }

  6.25% {
    transform: translate(-50%, -50%) translateX(117px) translateY(159px) translateZ(243px) scale(1.25);
    opacity: 1;
    z-index: 87.5;
  }

  12.5% {
    transform: translate(-50%, -50%) translateX(212px) translateY(127px) translateZ(212px) scale(1.15);
    opacity: 1;
    z-index: 75;
  }

  18.75% {
    transform: translate(-50%, -50%) translateX(272px) translateY(70px) translateZ(117px) scale(1.075);
    opacity: 1;
    z-index: 62.5;
  }

  25% {
    transform: translate(-50%, -50%) translateX(300px) translateY(0px) translateZ(0px) scale(1);
    opacity: 1;
    z-index: 50;
  }

  31.25% {
    transform: translate(-50%, -50%) translateX(272px) translateY(-70px) translateZ(-117px) scale(0.925);
    opacity: 0.875;
    z-index: 37.5;
  }

  37.5% {
    transform: translate(-50%, -50%) translateX(212px) translateY(-127px) translateZ(-212px) scale(0.85);
    opacity: 0.75;
    z-index: 25;
  }

  43.75% {
    transform: translate(-50%, -50%) translateX(117px) translateY(-159px) translateZ(-243px) scale(0.775);
    opacity: 0.625;
    z-index: 12.5;
  }

  50% {
    transform: translate(-50%, -50%) translateX(0px) translateY(-180px) translateZ(-300px) scale(0.7);
    opacity: 0.5;
    z-index: 0;
  }

  56.25% {
    transform: translate(-50%, -50%) translateX(-117px) translateY(-159px) translateZ(-243px) scale(0.775);
    opacity: 0.625;
    z-index: 12.5;
  }

  62.5% {
    transform: translate(-50%, -50%) translateX(-212px) translateY(-127px) translateZ(-212px) scale(0.85);
    opacity: 0.75;
    z-index: 25;
  }

  68.75% {
    transform: translate(-50%, -50%) translateX(-272px) translateY(-70px) translateZ(-117px) scale(0.925);
    opacity: 0.875;
    z-index: 37.5;
  }

  75% {
    transform: translate(-50%, -50%) translateX(-300px) translateY(0px) translateZ(0px) scale(1);
    opacity: 1;
    z-index: 50;
  }

  81.25% {
    transform: translate(-50%, -50%) translateX(-272px) translateY(70px) translateZ(117px) scale(1.075);
    opacity: 1;
    z-index: 62.5;
  }

  87.5% {
    transform: translate(-50%, -50%) translateX(-212px) translateY(127px) translateZ(212px) scale(1.15);
    opacity: 1;
    z-index: 75;
  }

  93.75% {
    transform: translate(-50%, -50%) translateX(-117px) translateY(159px) translateZ(243px) scale(1.25);
    opacity: 1;
    z-index: 87.5;
  }

  100% {
    transform: translate(-50%, -50%) translateX(0px) translateY(180px) translateZ(300px) scale(1.3);
    opacity: 1;
    z-index: 100;
  }
}

@keyframes iconOrbit4 {
  0% {
    transform: translate(-50%, -50%) translateX(0px) translateY(180px) translateZ(300px) scale(1.3);
    opacity: 1;
    z-index: 100;
  }

  6.25% {
    transform: translate(-50%, -50%) translateX(117px) translateY(159px) translateZ(243px) scale(1.25);
    opacity: 1;
    z-index: 87.5;
  }

  12.5% {
    transform: translate(-50%, -50%) translateX(212px) translateY(127px) translateZ(212px) scale(1.15);
    opacity: 1;
    z-index: 75;
  }

  18.75% {
    transform: translate(-50%, -50%) translateX(272px) translateY(70px) translateZ(117px) scale(1.075);
    opacity: 1;
    z-index: 62.5;
  }

  25% {
    transform: translate(-50%, -50%) translateX(300px) translateY(0px) translateZ(0px) scale(1);
    opacity: 1;
    z-index: 50;
  }

  31.25% {
    transform: translate(-50%, -50%) translateX(272px) translateY(-70px) translateZ(-117px) scale(0.925);
    opacity: 0.875;
    z-index: 37.5;
  }

  37.5% {
    transform: translate(-50%, -50%) translateX(212px) translateY(-127px) translateZ(-212px) scale(0.85);
    opacity: 0.75;
    z-index: 25;
  }

  43.75% {
    transform: translate(-50%, -50%) translateX(117px) translateY(-159px) translateZ(-243px) scale(0.775);
    opacity: 0.625;
    z-index: 12.5;
  }

  50% {
    transform: translate(-50%, -50%) translateX(0px) translateY(-180px) translateZ(-300px) scale(0.7);
    opacity: 0.5;
    z-index: 0;
  }

  56.25% {
    transform: translate(-50%, -50%) translateX(-117px) translateY(-159px) translateZ(-243px) scale(0.775);
    opacity: 0.625;
    z-index: 12.5;
  }

  62.5% {
    transform: translate(-50%, -50%) translateX(-212px) translateY(-127px) translateZ(-212px) scale(0.85);
    opacity: 0.75;
    z-index: 25;
  }

  68.75% {
    transform: translate(-50%, -50%) translateX(-272px) translateY(-70px) translateZ(-117px) scale(0.925);
    opacity: 0.875;
    z-index: 37.5;
  }

  75% {
    transform: translate(-50%, -50%) translateX(-300px) translateY(0px) translateZ(0px) scale(1);
    opacity: 1;
    z-index: 50;
  }

  81.25% {
    transform: translate(-50%, -50%) translateX(-272px) translateY(70px) translateZ(117px) scale(1.075);
    opacity: 1;
    z-index: 62.5;
  }

  87.5% {
    transform: translate(-50%, -50%) translateX(-212px) translateY(127px) translateZ(212px) scale(1.15);
    opacity: 1;
    z-index: 75;
  }

  93.75% {
    transform: translate(-50%, -50%) translateX(-117px) translateY(159px) translateZ(243px) scale(1.25);
    opacity: 1;
    z-index: 87.5;
  }

  100% {
    transform: translate(-50%, -50%) translateX(0px) translateY(180px) translateZ(300px) scale(1.3);
    opacity: 1;
    z-index: 100;
  }
}

@media (max-width: 768px) {
  .orbit-icons {
    width: 150%;
    height: 150%;
  }

  .orbit-icon {
    width: 40px;
    height: 40px;
  }

  .orbit-icon img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .avatar-icon {
    animation: iconOrbit1Mobile 9s linear infinite;
  }

  .edge-icon {
    animation: iconOrbit2Mobile 13s linear infinite;
    animation-delay: -2.25s;
  }

  .vscode-icon {
    animation: iconOrbit3Mobile 15s linear infinite;
    animation-delay: -4.5s;
  }

  .idea-icon {
    animation: iconOrbit4Mobile 11s linear infinite;
    animation-delay: -6.75s;
  }

  @keyframes iconOrbit1Mobile {
    0% {
      transform: translate(-50%, -50%) translateX(0px) translateY(140px) translateZ(240px) scale(1.2);
      opacity: 1;
      z-index: 100;
    }

    6.25% {
      transform: translate(-50%, -50%) translateX(94px) translateY(124px) translateZ(195px) scale(1.15);
      opacity: 1;
      z-index: 87.5;
    }

    12.5% {
      transform: translate(-50%, -50%) translateX(170px) translateY(99px) translateZ(170px) scale(1.1);
      opacity: 1;
      z-index: 75;
    }

    18.75% {
      transform: translate(-50%, -50%) translateX(218px) translateY(54px) translateZ(94px) scale(1.05);
      opacity: 1;
      z-index: 62.5;
    }

    25% {
      transform: translate(-50%, -50%) translateX(240px) translateY(0px) translateZ(0px) scale(1);
      opacity: 1;
      z-index: 50;
    }

    31.25% {
      transform: translate(-50%, -50%) translateX(218px) translateY(-54px) translateZ(-94px) scale(0.95);
      opacity: 0.875;
      z-index: 37.5;
    }

    37.5% {
      transform: translate(-50%, -50%) translateX(170px) translateY(-99px) translateZ(-170px) scale(0.9);
      opacity: 0.75;
      z-index: 25;
    }

    43.75% {
      transform: translate(-50%, -50%) translateX(94px) translateY(-124px) translateZ(-195px) scale(0.85);
      opacity: 0.625;
      z-index: 12.5;
    }

    50% {
      transform: translate(-50%, -50%) translateX(0px) translateY(-140px) translateZ(-240px) scale(0.8);
      opacity: 0.5;
      z-index: 0;
    }

    56.25% {
      transform: translate(-50%, -50%) translateX(-94px) translateY(-124px) translateZ(-195px) scale(0.85);
      opacity: 0.625;
      z-index: 12.5;
    }

    62.5% {
      transform: translate(-50%, -50%) translateX(-170px) translateY(-99px) translateZ(-170px) scale(0.9);
      opacity: 0.75;
      z-index: 25;
    }

    68.75% {
      transform: translate(-50%, -50%) translateX(-218px) translateY(-54px) translateZ(-94px) scale(0.95);
      opacity: 0.875;
      z-index: 37.5;
    }

    75% {
      transform: translate(-50%, -50%) translateX(-240px) translateY(0px) translateZ(0px) scale(1);
      opacity: 1;
      z-index: 50;
    }

    81.25% {
      transform: translate(-50%, -50%) translateX(-218px) translateY(54px) translateZ(94px) scale(1.05);
      opacity: 1;
      z-index: 62.5;
    }

    87.5% {
      transform: translate(-50%, -50%) translateX(-170px) translateY(99px) translateZ(170px) scale(1.1);
      opacity: 1;
      z-index: 75;
    }

    93.75% {
      transform: translate(-50%, -50%) translateX(-94px) translateY(124px) translateZ(195px) scale(1.15);
      opacity: 1;
      z-index: 87.5;
    }

    100% {
      transform: translate(-50%, -50%) translateX(0px) translateY(140px) translateZ(240px) scale(1.2);
      opacity: 1;
      z-index: 100;
    }
  }

  @keyframes iconOrbit2Mobile {
    0% {
      transform: translate(-50%, -50%) translateX(0px) translateY(140px) translateZ(240px) scale(1.2);
      opacity: 1;
      z-index: 100;
    }

    6.25% {
      transform: translate(-50%, -50%) translateX(94px) translateY(124px) translateZ(195px) scale(1.15);
      opacity: 1;
      z-index: 87.5;
    }

    12.5% {
      transform: translate(-50%, -50%) translateX(170px) translateY(99px) translateZ(170px) scale(1.1);
      opacity: 1;
      z-index: 75;
    }

    18.75% {
      transform: translate(-50%, -50%) translateX(218px) translateY(54px) translateZ(94px) scale(1.05);
      opacity: 1;
      z-index: 62.5;
    }

    25% {
      transform: translate(-50%, -50%) translateX(240px) translateY(0px) translateZ(0px) scale(1);
      opacity: 1;
      z-index: 50;
    }

    31.25% {
      transform: translate(-50%, -50%) translateX(218px) translateY(-54px) translateZ(-94px) scale(0.95);
      opacity: 0.875;
      z-index: 37.5;
    }

    37.5% {
      transform: translate(-50%, -50%) translateX(170px) translateY(-99px) translateZ(-170px) scale(0.9);
      opacity: 0.75;
      z-index: 25;
    }

    43.75% {
      transform: translate(-50%, -50%) translateX(94px) translateY(-124px) translateZ(-195px) scale(0.85);
      opacity: 0.625;
      z-index: 12.5;
    }

    50% {
      transform: translate(-50%, -50%) translateX(0px) translateY(-140px) translateZ(-240px) scale(0.8);
      opacity: 0.5;
      z-index: 0;
    }

    56.25% {
      transform: translate(-50%, -50%) translateX(-94px) translateY(-124px) translateZ(-195px) scale(0.85);
      opacity: 0.625;
      z-index: 12.5;
    }

    62.5% {
      transform: translate(-50%, -50%) translateX(-170px) translateY(-99px) translateZ(-170px) scale(0.9);
      opacity: 0.75;
      z-index: 25;
    }

    68.75% {
      transform: translate(-50%, -50%) translateX(-218px) translateY(-54px) translateZ(-94px) scale(0.95);
      opacity: 0.875;
      z-index: 37.5;
    }

    75% {
      transform: translate(-50%, -50%) translateX(-240px) translateY(0px) translateZ(0px) scale(1);
      opacity: 1;
      z-index: 50;
    }

    81.25% {
      transform: translate(-50%, -50%) translateX(-218px) translateY(54px) translateZ(94px) scale(1.05);
      opacity: 1;
      z-index: 62.5;
    }

    87.5% {
      transform: translate(-50%, -50%) translateX(-170px) translateY(99px) translateZ(170px) scale(1.1);
      opacity: 1;
      z-index: 75;
    }

    93.75% {
      transform: translate(-50%, -50%) translateX(-94px) translateY(124px) translateZ(195px) scale(1.15);
      opacity: 1;
      z-index: 87.5;
    }

    100% {
      transform: translate(-50%, -50%) translateX(0px) translateY(140px) translateZ(240px) scale(1.2);
      opacity: 1;
      z-index: 100;
    }
  }

  @keyframes iconOrbit3Mobile {
    0% {
      transform: translate(-50%, -50%) translateX(0px) translateY(140px) translateZ(240px) scale(1.2);
      opacity: 1;
      z-index: 100;
    }

    6.25% {
      transform: translate(-50%, -50%) translateX(94px) translateY(124px) translateZ(195px) scale(1.15);
      opacity: 1;
      z-index: 87.5;
    }

    12.5% {
      transform: translate(-50%, -50%) translateX(170px) translateY(99px) translateZ(170px) scale(1.1);
      opacity: 1;
      z-index: 75;
    }

    18.75% {
      transform: translate(-50%, -50%) translateX(218px) translateY(54px) translateZ(94px) scale(1.05);
      opacity: 1;
      z-index: 62.5;
    }

    25% {
      transform: translate(-50%, -50%) translateX(240px) translateY(0px) translateZ(0px) scale(1);
      opacity: 1;
      z-index: 50;
    }

    31.25% {
      transform: translate(-50%, -50%) translateX(218px) translateY(-54px) translateZ(-94px) scale(0.95);
      opacity: 0.875;
      z-index: 37.5;
    }

    37.5% {
      transform: translate(-50%, -50%) translateX(170px) translateY(-99px) translateZ(-170px) scale(0.9);
      opacity: 0.75;
      z-index: 25;
    }

    43.75% {
      transform: translate(-50%, -50%) translateX(94px) translateY(-124px) translateZ(-195px) scale(0.85);
      opacity: 0.625;
      z-index: 12.5;
    }

    50% {
      transform: translate(-50%, -50%) translateX(0px) translateY(-140px) translateZ(-240px) scale(0.8);
      opacity: 0.5;
      z-index: 0;
    }

    56.25% {
      transform: translate(-50%, -50%) translateX(-94px) translateY(-124px) translateZ(-195px) scale(0.85);
      opacity: 0.625;
      z-index: 12.5;
    }

    62.5% {
      transform: translate(-50%, -50%) translateX(-170px) translateY(-99px) translateZ(-170px) scale(0.9);
      opacity: 0.75;
      z-index: 25;
    }

    68.75% {
      transform: translate(-50%, -50%) translateX(-218px) translateY(-54px) translateZ(-94px) scale(0.95);
      opacity: 0.875;
      z-index: 37.5;
    }

    75% {
      transform: translate(-50%, -50%) translateX(-240px) translateY(0px) translateZ(0px) scale(1);
      opacity: 1;
      z-index: 50;
    }

    81.25% {
      transform: translate(-50%, -50%) translateX(-218px) translateY(54px) translateZ(94px) scale(1.05);
      opacity: 1;
      z-index: 62.5;
    }

    87.5% {
      transform: translate(-50%, -50%) translateX(-170px) translateY(99px) translateZ(170px) scale(1.1);
      opacity: 1;
      z-index: 75;
    }

    93.75% {
      transform: translate(-50%, -50%) translateX(-94px) translateY(124px) translateZ(195px) scale(1.15);
      opacity: 1;
      z-index: 87.5;
    }

    100% {
      transform: translate(-50%, -50%) translateX(0px) translateY(140px) translateZ(240px) scale(1.2);
      opacity: 1;
      z-index: 100;
    }
  }

  @keyframes iconOrbit4Mobile {
    0% {
      transform: translate(-50%, -50%) translateX(0px) translateY(140px) translateZ(240px) scale(1.2);
      opacity: 1;
      z-index: 100;
    }

    6.25% {
      transform: translate(-50%, -50%) translateX(94px) translateY(124px) translateZ(195px) scale(1.15);
      opacity: 1;
      z-index: 87.5;
    }

    12.5% {
      transform: translate(-50%, -50%) translateX(170px) translateY(99px) translateZ(170px) scale(1.1);
      opacity: 1;
      z-index: 75;
    }

    18.75% {
      transform: translate(-50%, -50%) translateX(218px) translateY(54px) translateZ(94px) scale(1.05);
      opacity: 1;
      z-index: 62.5;
    }

    25% {
      transform: translate(-50%, -50%) translateX(240px) translateY(0px) translateZ(0px) scale(1);
      opacity: 1;
      z-index: 50;
    }

    31.25% {
      transform: translate(-50%, -50%) translateX(218px) translateY(-54px) translateZ(-94px) scale(0.95);
      opacity: 0.875;
      z-index: 37.5;
    }

    37.5% {
      transform: translate(-50%, -50%) translateX(170px) translateY(-99px) translateZ(-170px) scale(0.9);
      opacity: 0.75;
      z-index: 25;
    }

    43.75% {
      transform: translate(-50%, -50%) translateX(94px) translateY(-124px) translateZ(-195px) scale(0.85);
      opacity: 0.625;
      z-index: 12.5;
    }

    50% {
      transform: translate(-50%, -50%) translateX(0px) translateY(-140px) translateZ(-240px) scale(0.8);
      opacity: 0.5;
      z-index: 0;
    }

    56.25% {
      transform: translate(-50%, -50%) translateX(-94px) translateY(-124px) translateZ(-195px) scale(0.85);
      opacity: 0.625;
      z-index: 12.5;
    }

    62.5% {
      transform: translate(-50%, -50%) translateX(-170px) translateY(-99px) translateZ(-170px) scale(0.9);
      opacity: 0.75;
      z-index: 25;
    }

    68.75% {
      transform: translate(-50%, -50%) translateX(-218px) translateY(-54px) translateZ(-94px) scale(0.95);
      opacity: 0.875;
      z-index: 37.5;
    }

    75% {
      transform: translate(-50%, -50%) translateX(-240px) translateY(0px) translateZ(0px) scale(1);
      opacity: 1;
      z-index: 50;
    }

    81.25% {
      transform: translate(-50%, -50%) translateX(-218px) translateY(54px) translateZ(94px) scale(1.05);
      opacity: 1;
      z-index: 62.5;
    }

    87.5% {
      transform: translate(-50%, -50%) translateX(-170px) translateY(99px) translateZ(170px) scale(1.1);
      opacity: 1;
      z-index: 75;
    }

    93.75% {
      transform: translate(-50%, -50%) translateX(-94px) translateY(124px) translateZ(195px) scale(1.15);
      opacity: 1;
      z-index: 87.5;
    }

    100% {
      transform: translate(-50%, -50%) translateX(0px) translateY(140px) translateZ(240px) scale(1.2);
      opacity: 1;
      z-index: 100;
    }
  }
}
</style>