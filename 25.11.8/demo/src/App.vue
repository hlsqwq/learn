<template>
  <div class="app">
    <myHead></myHead>
    <myMain></myMain>
    <myFoot></myFoot>
  </div>
</template>

<style>
.app {
  padding: 20PX;
  height: 500px;
  width: 100%;
  background-color: rgb(129, 201, 239);
}
</style>

<script>
import myHead from './components/myHead.vue';
import myMain from './components/myMain.vue';
import myFoot from './components/myFoot.vue';
export default {
  components: {
    myHead,
    myMain,
    myFoot,
  }
};

</script>