import Vue from 'vue'
import App from './App.vue'
import './styles/base.css' // css 样式重置
import './styles/common.css' // 公共全局样式
import './assets/iconfont/iconfont.css' // 字体图标的样式
import BaseGoodItem from './components/baseGoodItem.vue'



Vue.config.productionTip = false


Vue.component('baseGoodItem', BaseGoodItem)


new Vue({
  render: h => h(App),
}).$mount('#app')
