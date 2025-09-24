/**
 * 目标1：渲染图书列表
 *  1.1 获取数据
 *  1.2 渲染数据
 */


// <tr>
//   <td>1</td>
//   <td>JavaScript程序设计</td>
//   <td>马特·弗里斯比</td>
//   <td>人民邮电出版社</td>
//   <td>
// <span class="del">删除</span>
// <span class="edit">编辑</span>
//   </td>
// </tr>



const creator = 'admin'

function get_list() {
  axios({
    url: 'http://hmajax.itheima.net/api/books',
    method: 'get',
    params: {
      creator
    }
  }).then(res => {
    console.log(res);
    if (res.status === 200) {
      let list = res.data.data.map((e, i) => {
        return `<tr>
          <td>${i + 1}</td>
          <td>${e.bookname}</td>
          <td>${e.author}</td>
          <td>${e.publisher}</td>
          <td data-id="${e.id}">
            <span class="del">删除</span>
            <span class="edit">编辑</span>
          </td>
        </tr>`
      }).join(' ');
      document.querySelector('.list').innerHTML = list;
    }
  }).catch(err => {
    console.log(err);
  });
}
get_list()



//新增
const model = new bootstrap.Modal(document.querySelector('.add-modal'))
document.querySelector('.add-btn').addEventListener('click', add_book)
function add_book() {
  let data = serialize(document.querySelector('.add-form'), { hash: true, empty: true })
  if (data.bookname == undefined || data.bookname.trim().length === 0) {
    alert('请输入书名')
    return
  }
  if (data.author == undefined || data.author.trim().length === 0) {
    alert('请输入作者')
    return
  }
  if (data.publisher == undefined || data.publisher.trim().length === 0) {
    alert('请输入出版社')
    return
  }


  axios({
    url: 'http://hmajax.itheima.net/api/books',
    method: 'post',
    data: {
      ...data,
      creator
    }
  }).then(res => {
    console.log(res);
    get_list()
    document.querySelector('.add-form').reset()
    model.hide()
  }).catch(err => {
    console.log(err);
  })
}




// edit 编辑
const edit_model = new bootstrap.Modal(document.querySelector('.edit-modal'))
document.querySelector('.list').addEventListener('click', e => {
  if (!e.target.classList.contains('edit')) {
    return
  }
  let id = e.target.parentNode.dataset.id
  axios({
    url: `http://hmajax.itheima.net/api/books/${id}`,
    method: 'get',
  }).then(res => {
    console.log(res);
    let obj = res.data.data
    Object.keys(obj).forEach(key => {
      document.querySelector(`.edit-form .${key}`).value = obj[key]
    })
  }).catch(err => {
    console.log(err);
  })
  edit_model.show()
})

document.querySelector('.edit-btn').addEventListener('click', e => {
  let data = serialize(document.querySelector('.edit-form'), { hash: true, empty: true })
  if (data.bookname == undefined || data.bookname.trim().length === 0) {
    alert('请输入书名')
    return
  }
  if (data.author == undefined || data.author.trim().length === 0) {
    alert('请输入作者')
    return
  }
  if (data.publisher == undefined || data.publisher.trim().length === 0) {
    alert('请输入出版社')
    return
  }

  let id = document.querySelector('.edit-form .id').value
  axios({
    url: `http://hmajax.itheima.net/api/books/${id}`,
    method: 'put',
    data: {
      ...data,
      creator
    }
  }).then(res => {
    console.log(res);
    document.querySelector('.edit-form').reset()
    get_list()
    edit_model.hide()
  }).catch(err => {
    console.log("err");
  })
})





// 删除

document.querySelector('.list').addEventListener('click', e => {
  let id = e.target.parentNode.dataset.id
  axios({
    url: `http://hmajax.itheima.net/api/books/${id}`,
    method: 'delete',
  }).then(res => {
    console.log(res);
    get_list()
  })

})