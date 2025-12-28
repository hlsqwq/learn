<script setup>
import {  onMounted, provide, ref } from 'vue';



// const res=ref([1,2,3,4,5,6,7,8,9])
// let list=computed(()=>{
//   return res.value.filter(i=>i>2)
// })
// list= computed({
//   get:()=>{
//   return res.value.filter(i=>i>2)
    
//   },
//   set(val){
//     res.value=res.value.map(i=>i+val)
//   }
// })

// const res=ref({
//   name:'zhangsan',
//   age:18,
//   work:'teacher'
// })
// watch([()=>res.value.age],(val)=>{
//   console.log(val)
// },{
//   immediate:true,
//   deep:true
// })

import Son from '@/components/son.vue'
// const res=ref(100)
// const flag=ref()

// onMounted(()=>{
//   console.log(flag.value.res);
// })

const res=ref(100)
// provide('money',res)
// provide('change',()=>{
//   res.value++
// })

</script>
<template>
  <Son v-model="res"></Son>
</template>
<style lang="less" scoped></style>