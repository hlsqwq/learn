<template>
  <!-- 输入框 -->
  <header class="header">
    <h1>小黑记事本</h1>
    <input v-model='input' @keyup.enter="add" placeholder="请输入任务" class="new-todo" />
    <button class="add" @click="add">添加任务</button>
  </header>
</template>

<script>
export default {
  data() {
    return {
      input: ''
    }
  },
  methods: {
    add() {
      console.log('add')
      this.$emit('add', this.input)
      this.input = ''
    }
  }
}
</script>

<style></style>