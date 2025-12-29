<script setup>


import {useCounterStore} from '@/stores/counter'
import { storeToRefs } from 'pinia'

defineOptions({
  name:'appIndex'
})
const res= useCounterStore()
const {count}=storeToRefs(useCounterStore)
res.func()

</script>
<template>
  <div>{{res.count}}</div>
</template>
<style lang="less" scoped>

</style>