<template>
  <!-- 列表区域 -->
  <section class="main">
    <ul class="todo-list">
      <li v-for="(v, k) in list" :key="v.id" class="todo">
        <div class="view">
          <span class="index">
            {{ k + 1 }}.</span> <label>{{ v.name }}</label>
          <button @click="del(v.id)" class="destroy"></button>
        </div>
      </li>
    </ul>
  </section>
</template>

<script>
export default {
  props: {
    list: Array
  },
  methods: {
    del(id) {
      this.$emit('del', id)
    }
  }
}
</script>

<style></style>