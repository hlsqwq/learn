<!-- <template>

  <myDemo></myDemo>

</template>

<script>
import myDemo from './components/myDemo.vue'
export default {
  components: { myDemo }
}
</script>

<style></style> -->

<!-- <MyTable :obj="list">
        <button>删除</button>
    </MyTable>

<template>
  <div>
    <MyTable :obj="list">
      <template #default="obj">
        <button @click="del(obj.id)">删除</button>
      </template>
    </MyTable>
    <MyTable :obj="list2">
      <template #default="obj">
        <button @click="get(obj.id)">查看</button>
      </template>
    </MyTable>

  </div>
</template>

<script>
import MyTable from './components/MyTable.vue'
export default {
  methods: {
    del(id) {
      this.list = this.list.filter(item => item.id !== id)
    },
    get(id) {
      const user = this.list2.find(item => item.id === id)
      alert(`姓名：${user.name}，年龄：${user.age}`)
    }
  },
  data() {
    return {
      list: [
        { id: 1, name: '张小花', age: 18 },
        { id: 2, name: '孙大明', age: 19 },
        { id: 3, name: '刘德忠', age: 17 },
      ],
      list2: [
        { id: 1, name: '赵小云', age: 18 },
        { id: 2, name: '刘蓓蓓', age: 19 },
        { id: 3, name: '姜肖泰', age: 17 },
      ]
    }
  },
  components: {
    MyTable
  }
}
</script> -->









<!-- <template>
  <div class="table-case">
    <table class="my-table">
      <thead>
        <tr>
          <th>编号</th>
          <th>名称</th>
          <th>图片</th>
          <th width="100px">标签</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>1</td>
          <td>梨皮朱泥三绝清代小品壶经典款紫砂壶</td>
          <td>
            <img src="https://yanxuan-item.nosdn.127.net/f8c37ffa41ab1eb84bff499e1f6acfc7.jpg" />
          </td>
          <td>
            <myTag></myTag>
          </td>
        </tr>

      </tbody>
    </table>
  </div>
</template>

<script>
import myTag from './components/myTag.vue'
export default {
  name: 'TableCase',
  components: { myTag },
  data() {
    return {
      goods: [
        {
          id: 101,
          picture:
            'https://yanxuan-item.nosdn.127.net/f8c37ffa41ab1eb84bff499e1f6acfc7.jpg',
          name: '梨皮朱泥三绝清代小品壶经典款紫砂壶',
          tag: '茶具',
        },
        {
          id: 102,
          picture:
            'https://yanxuan-item.nosdn.127.net/221317c85274a188174352474b859d7b.jpg',
          name: '全防水HABU旋钮牛皮户外徒步鞋山宁泰抗菌',
          tag: '男鞋',
        },
        {
          id: 103,
          picture:
            'https://yanxuan-item.nosdn.127.net/cd4b840751ef4f7505c85004f0bebcb5.png',
          name: '毛茸茸小熊出没，儿童羊羔绒背心73-90cm',
          tag: '儿童服饰',
        },
        {
          id: 104,
          picture:
            'https://yanxuan-item.nosdn.127.net/56eb25a38d7a630e76a608a9360eec6b.jpg',
          name: '基础百搭，儿童套头针织毛衣1-9岁',
          tag: '儿童服饰',
        },
      ],
    }
  },
}
</script>

<style scoped>
.table-case {
  width: 1000px;
  margin: 50px auto;

  img {
    width: 100px;
    height: 100px;
    object-fit: contain;
    vertical-align: middle;
  }

  .my-table {
    width: 100%;
    border-spacing: 0;

    img {
      width: 100px;
      height: 100px;
      object-fit: contain;
      vertical-align: middle;
    }

    th {
      background: #f5f5f5;
      border-bottom: 2px solid #069;
    }

    td {
      border-bottom: 1px dashed #ccc;
    }

    td,
    th {
      text-align: center;
      padding: 10px;
      transition: all 0.5s;

      &.red {
        color: red;
      }
    }

    .none {
      height: 100px;
      line-height: 100px;
      color: #999;
    }
  }


}
</style> -->







<template>
  <div>
    <div class="content">
      <button @click="$router.push('my')">my</button>
      <button @click="$router.push('music')">music</button>
    </div>

    <div class="top">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
export default {

}
</script>

<style>
button {
  margin: 20px;
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
  margin-right: 20px;
}

.content {
  display: flex;
  text-align: center;
}
</style>