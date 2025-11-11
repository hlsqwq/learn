<template>
  <div class="my-tag">
    <input v-if="flag" @blur="flag = !flag" class="input" @keyup.enter="flag = !flag" v-model="msg" type="text"
      placeholder="输入标签" />
    <div v-else @dblclick="flag = !flag" class="text">
      {{ msg }}
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      flag: true,
      msg: "",
    }
  },
}
</script>

<style scoped>
.my-tag {
  cursor: pointer;

  .input {
    appearance: none;
    outline: none;
    border: 1px solid #ccc;
    width: 100px;
    height: 40px;
    box-sizing: border-box;
    padding: 10px;
    color: #666;

    &::placeholder {
      color: #666;
    }
  }
}
</style>