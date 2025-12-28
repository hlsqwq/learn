<template>
  <div class="box">
    <div class="sub" @click="sub">-</div>
    <input :value="count" type="text" class="in" @change="func">
    <div class="plus" @click="plus">+</div>
  </div>
</template>

<script>
export default {
  props: {
    count: {
      type: Number,
      default: 1
    }
  },
  methods: {
    func (e) {
      const val = +e.target.value
      if (isNaN(val)) {
        e.target.value = this.count
        return
      }
      this.$emit('update:count', val)
    },
    sub () {
      if (this.count > 1) {
        this.$emit('update:count', this.count - 1)
      }
    },
    plus () {
      this.$emit('update:count', this.count + 1)
    }
  }
}
</script>

<style lang="less" scoped>
.box{
  width: 110px;
  display: flex;
  .sub,.plus{
    width: 30px;
    height: 30px;
    text-align: center;
    line-height: 30px;
    outline: none;
    border: none;
    background-color: #efefef;
  }
  .in{
    width: 40px;
    margin: 0 5px;
    height: 30px;
    text-align: center;
    line-height: 30px;
    outline: none;
    border: none;
    background-color: #efefef;
  }
}
</style>
