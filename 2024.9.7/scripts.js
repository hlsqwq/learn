// 简单的轮播图功能
let currentIndex = 0;
const carouselInner = document.querySelector('.carousel-inner');
const items = document.querySelectorAll('.carousel-item');
const totalItems = items.length;

function showNextItem() {
  currentIndex = (currentIndex + 1) % totalItems;
  carouselInner.style.transform = `translateX(-${currentIndex * 100}%)`;
}

setInterval(showNextItem, 3000); // 每3秒切换一次

// 简单的滚动效果
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
  anchor.addEventListener('click', function (e) {
    e.preventDefault();
    document.querySelector(this.getAttribute('href')).scrollIntoView({
      behavior: 'smooth'
    });
  });
});