<template>
  <div id="app">
    <div>{{num}}</div>
      <son></son>
      <son1></son1>
    </div>
</template>

<script>
  import son from './components/thisSon.vue'
  import son1 from './components/thisSon1.vue'
  import { mapState } from 'vuex'


export default {
  name: 'App',
  created() {
    console.log(this.$store);
    
  },
  computed: {
    ...mapState(['num']),

  },
  data() {
    return {

    }
  },
  components: {
    son,
    son1
  }
}
</script>

<style>

</style>