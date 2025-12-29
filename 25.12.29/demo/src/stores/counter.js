import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useCounterStore = defineStore('counter', () => {
  const count = ref(0)
  const doubleCount = computed(() => count.value * 2)
  function func(){
    setTimeout(()=>{
      count.value=100
    },2000)
  }

  return { count, doubleCount,func }
},{
  piniaPluginPersistedstate:true
})

