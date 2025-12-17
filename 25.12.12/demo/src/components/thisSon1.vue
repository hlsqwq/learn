<template>
  <div>
    son component{{ num }}
  </div>
</template>

<script>
  import { mapState } from 'vuex'
export default {
  data() {
    return {

    }
  },
  computed: {
    ...mapState(['num'])
  }
}
</script>

<style>

</style>