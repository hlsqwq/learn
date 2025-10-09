/**
 * 目标1：验证码登录
 * 1.1 在 utils/request.js 配置 axios 请求基地址
 * 1.2 收集手机号和验证码数据
 * 1.3 基于 axios 调用验证码登录接口
 * 1.4 使用 Bootstrap 的 Alert 警告框反馈结果给用户
 */



const btn = document.querySelector("body > div.login-wrap > div:nth-child(2) > form > div:nth-child(3) > button")
const form = document.querySelector("body > div.login-wrap > div:nth-child(2) > form")
function login() {
  const data = serialize(form, { hash: true, empty: true })
  //todo 数据验证
  axios({
    url: '/v1_0/authorizations',
    method: 'post',
    data
  }).then(res => {
    console.log(res);
    myAlert(true, 'login success')
    console.log(res.data.token);

    localStorage.setItem('token', res.data.token)
    location.href = '../content/index.html'
  }).catch(err => {
    console.log(err);
    myAlert(false, err.response.data.message)
  })
}
btn.addEventListener('click', login)