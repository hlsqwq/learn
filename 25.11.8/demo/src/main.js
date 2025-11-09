import Vue from 'vue'
import App from './App.vue'
import myButton from './components/myButton.vue'


Vue.component('myButton', myButton)

Vue.config.productionTip = false

new Vue({
  render: h => h(App),
}).$mount('#app')



