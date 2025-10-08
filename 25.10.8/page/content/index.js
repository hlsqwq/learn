/**
 * 目标1：获取文章列表并展示
 *  1.1 准备查询参数对象
 *  1.2 获取文章列表数据
 *  1.3 展示到指定的标签结构中
 */


const tbody = document.querySelector("body > div > div.main > div.content > div:nth-child(2) > table > tbody")
function get_list(status, channel_id, page = 1, per_page = 5) {
  axios({
    url: '/v1_0/mp/articles',
    params: {
      status,
      channel_id,
      page,
      per_page
    }
  }).then(res => {
    const list = res.data.results
    const str = list.map(i => {
      return `<tr data-id=${i.id}>
                <td>
                  <img src="${i.cover.images[0]}" alt="">
                </td>
                <td>${i.title}</td>
                <td>
                ${i.status == 1 ? '<span class="badge text-bg-primary">待审核</span>'
          : '<span class="badge text-bg-success">审核通过</span>'} 
                </td>
                <td>
                  <span>${i.pubdate}</span>
                </td>
                <td>
                  <span>${i.read_count}</span>
                </td>
                <td>
                  <span>${i.comment_count}</span>
                </td>
                <td>
                  <span>${i.like_count}</span>
                </td>
                <td>
                  <i class="bi bi-pencil-square edit"></i>
                  <i class="bi bi-trash3 del"></i>
                </td>
              </tr>`
    }).join('')
    tbody.innerHTML = str
    document.querySelector("body > div > div.main > div.content > div:nth-child(2) > nav > ul > li:nth-child(4) > span")
      .innerHTML = `共${res.data.total_count}条`
  })
}
get_list()




/**
 * 目标2：筛选文章列表
 *  2.1 设置频道列表数据
 *  2.2 监听筛选条件改变，保存查询信息到查询参数对象
 *  2.3 点击筛选时，传递查询参数对象到服务器
 *  2.4 获取匹配数据，覆盖到页面展示
 */

const channel = document.querySelector('.form-select')
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
const btn = document.querySelector("body > div > div.main > div.content > div:nth-child(1) > div.body > form > div:nth-child(3) > button")
const form = document.querySelector('.sel-form')
btn.addEventListener('click', function () {
  const data = serialize(form, { hash: true, empty: true })
  get_list(data.status, data.channel_id)
})






/**
 * 目标3：分页功能
 *  3.1 保存并设置文章总条数
 *  3.2 点击下一页，做临界值判断，并切换页码参数并请求最新数据
 *  3.3 点击上一页，做临界值判断，并切换页码参数并请求最新数据
 */

/**
 * 目标4：删除功能
 *  4.1 关联文章 id 到删除图标
 *  4.2 点击删除时，获取文章 id
 *  4.3 调用删除接口，传递文章 id 到服务器
 *  4.4 重新获取文章列表，并覆盖展示
 *  4.5 删除最后一页的最后一条，需要自动向前翻页
 */

// 点击编辑时，获取文章 id，跳转到发布文章页面传递文章 id 过去

