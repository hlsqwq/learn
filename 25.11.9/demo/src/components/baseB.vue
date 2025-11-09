<template>
  <div>
    component b
    <button @click="send">click</button>
  </div>
</template>

<script>
import bus from '../utils/bus.js'
export default {
  methods: {
    send() {
      bus.$emit('send', 'hello from b')
    }
  }
}
</script>

<style></style>