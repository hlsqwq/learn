<template>
  <div id="layoutIndex">

    <router-view></router-view>

    <van-tabbar route active-color="#ee0a24" inactive-color="#000">
      <van-tabbar-item to="/home"  icon="wap-home">首页</van-tabbar-item>
      <van-tabbar-item to="/classify"  icon="cluster">分类</van-tabbar-item>
      <van-tabbar-item to="/cart"  icon="shopping-cart">购物车</van-tabbar-item>
      <van-tabbar-item to="/my"  icon="contact">我的</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script>
export default {
  name: 'layoutIndex',
  data () {
    return {
    }
  }
}
</script>

<style>

</style>
