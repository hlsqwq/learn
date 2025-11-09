<template>
  <div>
    son component
    <div>
      姓名：{{ obj.name }}
    </div>
    <div>
      年龄：{{ obj.age }}
    </div>
    <button @click="func">click</button>
  </div>
</template>

<script>
export default {
  data() {
    return {}
  },
  props: ['obj'],
  methods: {
    func() {
      this.$emit('change', { name: 'qwe', age: 30 })
    }
  }
}
</script>

<style></style>