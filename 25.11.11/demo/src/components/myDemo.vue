<template>
  <div>
    <div v-color="color1">first</div>
    <div v-color="color2">second</div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      color1: 'red',
      color2: 'green'
    }
  },
  directives: {
    color: {
      inserted(el, binding) {
        el.style.color = binding.value
      },
      update(el, binding) {
        el.style.color = binding.value
      }
    }
  }
}
</script>

<style></style>