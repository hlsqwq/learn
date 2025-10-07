/**
 * 目标1：完成省市区下拉列表切换
 *  1.1 设置省份下拉菜单数据
 *  1.2 切换省份，设置城市下拉菜单数据，清空地区下拉菜单
 *  1.3 切换城市，设置地区下拉菜单数据
 */


const province = document.querySelector("body > div > div > form > div:nth-child(1) > select.province")
const city = document.querySelector("body > div > div > form > div:nth-child(1) > select.city")
const area = document.querySelector("body > div > div > form > div:nth-child(1) > select.area")
function get_province() {
  axios({
    url: 'http://hmajax.itheima.net/api/province'
  }).then(({ data }) => {
    console.log(data);
    const str = data.list.map(i => {
      return `<option value="${i}">${i}</option>`
    }).join('')
    province.innerHTML += str
  })
}
get_province()

function clear(obj, str) {
  obj.innerHTML = `<option value="${str}">${str}</option>`
}

function clear_province() {
  clear(city, '城市')
  clear(area, '地区')
}
function clear_city() {
  clear(area, '地区')
}

province.addEventListener('change', function () {
  axios({
    url: 'http://hmajax.itheima.net/api/city',
    params: { pname: this.value }
  }).then(({ data }) => {
    console.log(data);
    clear_province()
    const str = data.list.map(i => {
      return `<option value="${i}">${i}</option>`
    }).join('')
    city.innerHTML += str
  })
})


city.addEventListener('change', function () {
  axios({
    url: 'http://hmajax.itheima.net/api/area',
    params: { pname: province.value, cname: this.value }
  }).then(({ data }) => {
    console.log(data);
    clear_city()
    const str = data.list.map(i => {
      return `<option value="${i}">${i}</option>`
    }).join('')
    area.innerHTML += str
  })
})


const form = document.querySelector("body > div > div > form")
form.addEventListener('submit', function (e) {
  e.preventDefault()
  const data = serialize(form, { hash: true, empty: true })
  console.log(data);
  axios({
    url: 'http://hmajax.itheima.net/api/feedback',
    method: 'post',
    data
  }).then(res => {
    console.log(res.data);
    alert(res.data.message)
    form.reset()
  }).catch(err => {
    alert(err)
  })

})