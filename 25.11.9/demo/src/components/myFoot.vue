<template>
  <!-- 统计和清空 -->
  <footer class="footer">
    <!-- 统计 -->
    <span class="todo-count">合 计:<strong> {{ total }} </strong></span>
    <!-- 清空 -->
    <button @click="del_all" class="clear-completed">
      清空任务
    </button>
  </footer>
</template>

<script>
export default {
  props: {
    total: Number
  },
  methods: {
    del_all() {
      this.$emit('del_all')
    }
  }
}
</script>

<style></style>