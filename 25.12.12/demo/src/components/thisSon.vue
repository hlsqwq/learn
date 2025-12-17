<template>
  <div>
    son coasdasdasdamponent{{ $store.state.num }}
    <button @click="func">click</button>
  </div>
</template>

<script>
export default {
  methods: {
    func() {
      this.$store.dispatch('change_async',666)
    }
  }
}
</script>

<style>

</style>