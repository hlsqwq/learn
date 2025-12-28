<script setup>
import { inject, } from 'vue';

  defineOptions({
    name:'mySon'
  })
  

  const res=defineModel()


</script>
<template>
  <div>{{ res }}</div>
  <button @click="res++">clikc</button>
</template>
<style lang="less" scoped>

</style>