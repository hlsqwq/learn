<template>
  <div>
    component a
    {{ msg }}
  </div>
</template>

<script>
import bus from '../utils/bus.js'
export default {
  created() {
    bus.$on('send', (data) => {
      this.msg = data
    })
  },
  data() {
    return {
      msg: ''
    }
  }
}
</script>

<style></style>