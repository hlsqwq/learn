<!-- <template>
  <mySon :obj="user" @change="user = $event"></mySon>
</template>

<script>
import mySon from './components/mySon.vue'
export default {
  components: { mySon },
  data() {
    return {
      user: { name: 'asd', age: 20 }
    }
  },
}
</script>

<style></style>



<template>
  主体区域
  <section id="app">
    <myHead @add="add"></myHead>
    <myList :list="list" @del="del"></myList>
    <myFoot :total="total" @del_all="del_all"></myFoot>
  </section>
</template>

<script>
import myHead from './components/myHead.vue'
import myList from './components/myList.vue'
import myFoot from './components/myFoot.vue'

export default {
  components: { myHead, myList, myFoot },
  computed: {
    total() {
      return this.list.length
    }
  },
  watch: {
    list: {
      deep: true,
      handler(newVal) {
        localStorage.setItem('list', JSON.stringify(newVal))
      }
    }
  },
  methods: {
    add(item) {
      this.list.push({
        id: this.list[this.list.length - 1].id + 1,
        name: item
      })
    },
    del(id) {
      this.list = this.list.filter(item => item.id !== id)
    },
    del_all() {
      this.list = []
    }
  },
  data() {
    return {
      list: JSON.parse(localStorage.getItem('list')) || [
        {
          id: 1,
          name: '吃饭饭',
        },
        {
          id: 2,
          name: '睡觉觉',
        },
        {
          id: 3,
          name: '打豆豆',
        }
      ]
    }
  }
}
</script>

<style></style> -->

























<template>
  <div>
    <baseA></baseA>
    <baseB></baseB>
  </div>
</template>

<script>
import baseA from './components/baseA.vue'
import baseB from './components/baseB.vue'
export default {
  components: { baseA, baseB }
}
</script>

<style></style>