/**
 * 目标1：设置频道下拉菜单
 *  1.1 获取频道列表数据
 *  1.2 展示到下拉菜单中
 */



const { createEditor, createToolbar } = window.wangEditor

const editorConfig = {
  placeholder: 'Type here...',
  onChange(editor) {
    const html = editor.getHtml()
    console.log('editor content', html)
    // 也可以同步到 <textarea>
    document.querySelector('.publish-content').innerHTML = html
  },
}

const editor = createEditor({
  selector: '#editor-container',
  html: '<p><br></p>',
  config: editorConfig,
  mode: 'default', // or 'simple'
})

const toolbarConfig = {}

const toolbar = createToolbar({
  editor,
  selector: '#toolbar-container',
  config: toolbarConfig,
  mode: 'default', // or 'simple'
})


const channel = document.querySelector('#channel_id')
function get_classify() {
  axios({ url: '/v1_0/channels' }).then(res => {
    console.log(res);

    const str = res.data.channels.map(i => {
      return `<option value="${i.id}">${i.name}</option>`
    }).join('')
    channel.innerHTML = '<option value="" selected>请选择文章频道</option>' + str
  }).catch(err => {
    console.log(err);

  })
}
get_classify()





/**
 * 目标2：文章封面设置
 *  2.1 准备标签结构和样式
 *  2.2 选择文件并保存在 FormData
 *  2.3 单独上传图片并得到图片 URL 网址
 *  2.4 回显并切换 img 标签展示（隐藏 + 号上传标签）
 */




const img_in = document.querySelector('.img-file')
const img = document.querySelector('.rounded')

img_in.addEventListener('change', function () {
  if (!this.files[0]) {
    return
  }
  const data = new FormData()
  data.append('image', this.files[0])
  axios({
    url: '/v1_0/upload',
    method: 'post',
    data
  }).then(res => {
    const url = res.data.url
    console.log(url);
    img.src = url
    img.classList.add('show')
    document.querySelector('.place').classList.add('hide')
  })
})


img.addEventListener('click', function () {
  img_in.click()
})




/**
 * 目标3：发布文章保存
 *  3.1 基于 form-serialize 插件收集表单数据对象
 *  3.2 基于 axios 提交到服务器保存
 *  3.3 调用 Alert 警告框反馈结果给用户
 *  3.4 重置表单并跳转到列表页
 */

const btn = document.querySelector("body > div.wrap > div.main > div.content > div > div.body > form > div:nth-child(6) > button")
const form = document.querySelector("body > div.wrap > div.main > div.content > div > div.body > form")
btn.addEventListener('click', function () {
  const data = serialize(form, { hash: true, empty: true })
  delete data.id
  data.cover = {
    type: 1,
    images: [img.src]
  }



  axios({
    url: '/v1_0/mp/articles',
    method: 'post',
    data
  }).then(res => {
    myAlert(true, 'save article success')
    img.src = ''
    img.classList.remove('show')
    document.querySelector('.place').classList.remove('hide')
    editor.setHtml('')
    form.reset()
    location.href = '../content/index.html'
  }).catch(err => {
    console.log(err.response.data.message);
    myAlert(false, 'save article fail')
  })
})






/**
 * 目标4：编辑-回显文章
 *  4.1 页面跳转传参（URL 查询参数方式）
 *  4.2 发布文章页面接收参数判断（共用同一套表单）
 *  4.3 修改标题和按钮文字
 *  4.4 获取文章详情数据并回显表单
 */

/**
 * 目标5：编辑-保存文章
 *  5.1 判断按钮文字，区分业务（因为共用一套表单）
 *  5.2 调用编辑文章接口，保存信息到服务器
 *  5.3 基于 Alert 反馈结果消息给用户
 */
