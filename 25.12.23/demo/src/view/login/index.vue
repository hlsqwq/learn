<template>
  <div class="login">
    <van-nav-bar title="会员登录" left-arrow @click-left="$router.go(-1)" />

    <div class="container">
      <div class="title">
        <h3>手机号登录</h3>
        <p>未注册的手机号登录后将自动注册</p>
      </div>

      <div class="form">
        <div class="form-item">
          <input v-model="mobile" class="inp" maxlength="11" placeholder="请输入手机号码" type="text">
        </div>
        <div class="form-item">
          <input v-model="picCode" class="inp" maxlength="5" placeholder="请输入图形验证码" type="text">
          <img v-if="picUrl" :src="picUrl" @click="getPic" alt="">
        </div>
        <div class="form-item">
          <input v-model="msgCode" class="inp" placeholder="请输入短信验证码" type="text">
          <button @click="getCode">
            {{ second === totalSecond ? '获取验证码' : second + '秒后重新发送'}}
          </button>
        </div>
      </div>

      <div class="login-btn" @click="login">登录</div>
    </div>
  </div>
</template>

<script>
import { getPicUrl, getSmsCode, login } from '@/api/login'

export default {
  name: 'loginIndex',
  data () {
    return {
      picCode: '',
      picUrl: '',
      picKey: '',
      msgCode: '',
      mobile: '',
      second: 5,
      totalSecond: 5,
      time: 0,
      is_call: false
    }
  },
  computed: {
    backUrl () {
      return this.$route.query.backUrl
    }
  },
  async created () {
    this.getPic()
  },
  methods: {
    async getPic () {
      const { data: { base64, key } } = await getPicUrl()
      this.picUrl = base64
      this.picKey = key
    },
    async getCode () {
      if (this.mobile.length !== 11) {
        this.$toast('请输入正确的手机号')
        return
      } else if (this.picCode.length === 0) {
        this.$toast('请输入正确的图形验证码')
        return
      } else if (this.second < this.totalSecond) {
        this.$toast('请勿重复获取验证码')
        return
      }
      if (this.is_call) {
        return
      }
      this.is_call = true

      this.time = setInterval(() => {
        if (this.second > 0) {
          this.second--
        } else {
          this.is_call = false
          clearInterval(this.time)
          this.second = this.totalSecond
        }
      }, 1000)
      await getSmsCode(this.picCode, this.picKey, this.mobile)
      this.$toast('验证码发送中...')
    },
    async login () {
      if (this.mobile.length !== 11) {
        this.$toast('请输入正确的手机号')
        return
      } else if (this.picCode.length === 0) {
        this.$toast('请输入正确的图形验证码')
        return
      } else if (this.msgCode.length === 0) {
        this.$toast('请输入验证码')
        return
      }

      const res = await login(this.mobile, this.msgCode)
      this.$store.commit('user/setToken', { token: res.data.token, userId: res.data.userId })
      console.log(res.data)
      this.$toast('登录成功')
      if (this.backUrl) {
        this.$router.replace(this.backUrl)
        return
      }
      this.$router.push('/')
    }
  },
  destroyed () {
    clearInterval(this.time)
  }
}
</script>

<style lang="less" scoped>
.container {
  padding: 49px 29px;

  .title {
    margin-bottom: 20px;
    h3 {
      font-size: 26px;
      font-weight: normal;
    }
    p {
      line-height: 40px;
      font-size: 14px;
      color: #b8b8b8;
    }
  }

  .form-item {
    border-bottom: 1px solid #f3f1f2;
    padding: 8px;
    margin-bottom: 14px;
    display: flex;
    align-items: center;
    .inp {
      display: block;
      border: none;
      outline: none;
      height: 32px;
      font-size: 14px;
      flex: 1;
    }
    img {
      width: 94px;
      height: 31px;
    }
    button {
      height: 31px;
      border: none;
      font-size: 13px;
      color: #cea26a;
      background-color: transparent;
      padding-right: 9px;
    }
  }

  .login-btn {
    width: 100%;
    height: 42px;
    margin-top: 39px;
    background: linear-gradient(90deg,#ecb53c,#ff9211);
    color: #fff;
    border-radius: 39px;
    box-shadow: 0 10px 20px 0 rgba(0,0,0,.1);
    letter-spacing: 2px;
    display: flex;
    justify-content: center;
    align-items: center;
  }
}
</style>
