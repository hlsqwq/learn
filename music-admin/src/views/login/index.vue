<template>
  <div class="min-h-screen bg-[#fbfbfd] flex items-center justify-center p-4 overflow-hidden relative">
    <!-- 纯装饰性背景 -->
    <div
      class="absolute -top-40 -left-40 w-96 h-96 bg-blue-400 rounded-full mix-blend-multiply filter blur-3xl opacity-20 animate-blob">
    </div>
    <div
      class="absolute top-40 -right-40 w-96 h-96 bg-purple-400 rounded-full mix-blend-multiply filter blur-3xl opacity-20 animate-blob animation-delay-2000">
    </div>
    <div
      class="absolute -bottom-40 left-20 w-96 h-96 bg-pink-400 rounded-full mix-blend-multiply filter blur-3xl opacity-20 animate-blob animation-delay-4000">
    </div>

    <div
      class="glass w-full max-w-md rounded-2xl p-10 shadow-[0_8px_32px_rgba(0,0,0,0.04)] relative z-10 transition-transform duration-500 hover:scale-[1.01]">
      <!-- 头部 -->
      <div class="text-center mb-8">
        <el-icon class="text-5xl text-blue-600 mb-4 drop-shadow-md">
          <Headset />
        </el-icon>
        <h1 class="text-3xl font-extrabold text-gray-900 tracking-tight">Music Admin</h1>
        <p class="text-sm text-gray-400 mt-2 font-medium">{{ isLogin ? '登录以管理您的音乐世界' : '创建您的账户' }}</p>
      </div>

      <!-- 切换标签 -->
      <div class="flex mb-6 bg-gray-100 rounded-xl p-1">
        <button
          :class="['flex-1 py-2 text-sm font-medium rounded-lg transition-all', isLogin ? 'bg-white text-blue-600 shadow-sm' : 'text-gray-500']"
          @click="switchToLogin">
          登录
        </button>
        <button
          :class="['flex-1 py-2 text-sm font-medium rounded-lg transition-all', !isLogin ? 'bg-white text-blue-600 shadow-sm' : 'text-gray-500']"
          @click="switchToRegister">
          注册
        </button>
      </div>

      <!-- 登录表单 -->
      <el-form v-if="isLogin" ref="loginFormRef" :model="loginForm" :rules="loginRules" size="large" class="space-y-5">
        <el-form-item prop="account">
          <el-input v-model="loginForm.account" placeholder="请输入账号" :prefix-icon="User" class="!rounded-xl"
            clearable />
        </el-form-item>

        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" :prefix-icon="Lock"
            class="!rounded-xl" show-password @keyup.enter="handleLogin" />
        </el-form-item>

        <el-form-item prop="checkCode">
          <div class="flex w-full space-x-4">
            <el-input v-model="loginForm.checkCode" placeholder="请输入验证码" :prefix-icon="Key" class="flex-1 !rounded-xl"
              @keyup.enter="handleLogin" />
            <div
              class="w-32 h-[40px] bg-gray-100 rounded-xl cursor-pointer overflow-hidden shadow-inner flex items-center justify-center border border-gray-200"
              @click="fetchCaptcha" title="点击刷新验证码">
              <img v-if="captchaImg" :src="captchaImg" class="w-full h-full object-cover" alt="验证码" />
              <el-icon v-else class="text-gray-400 animate-spin">
                <Loading />
              </el-icon>
            </div>
          </div>
        </el-form-item>

        <el-button type="primary"
          class="w-full !rounded-xl !h-12 text-base font-bold shadow-[0_4px_14px_rgba(59,130,246,0.4)] hover:shadow-[0_6px_20px_rgba(59,130,246,0.6)] transition-all"
          :loading="loading" @click="handleLogin">
          登 录
        </el-button>
      </el-form>

      <!-- 注册表单 -->
      <el-form v-else ref="registerFormRef" :model="registerForm" :rules="registerRules" size="large" class="space-y-5">
        <el-form-item prop="name">
          <el-input v-model="registerForm.name" placeholder="请输入昵称" :prefix-icon="User" class="!rounded-xl" clearable />
        </el-form-item>

        <el-form-item prop="account">
          <el-input v-model="registerForm.account" placeholder="请输入账号" :prefix-icon="UserFilled" class="!rounded-xl"
            clearable />
        </el-form-item>

        <el-form-item prop="passwd">
          <el-input v-model="registerForm.passwd" type="password" placeholder="请输入密码" :prefix-icon="Lock"
            class="!rounded-xl" show-password />
        </el-form-item>

        <el-form-item prop="confirmPasswd">
          <el-input v-model="registerForm.confirmPasswd" type="password" placeholder="请确认密码" :prefix-icon="Lock"
            class="!rounded-xl" show-password @keyup.enter="handleRegister" />
        </el-form-item>

        <el-form-item prop="sex">
          <el-radio-group v-model="registerForm.sex" class="w-full">
            <el-radio value="男">男</el-radio>
            <el-radio value="女">女</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-button type="primary"
          class="w-full !rounded-xl !h-12 text-base font-bold shadow-[0_4px_14px_rgba(59,130,246,0.4)] hover:shadow-[0_6px_20px_rgba(59,130,246,0.6)] transition-all"
          :loading="loading" @click="handleRegister">
          注 册
        </el-button>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { User, Lock, Key, Headset, Loading, UserFilled } from '@element-plus/icons-vue'
import request from '@/utils/request'

const router = useRouter()
const loginFormRef = ref<FormInstance>()
const registerFormRef = ref<FormInstance>()
const loading = ref(false)
const isLogin = ref(true)

// 验证码图片与 Key
const captchaImg = ref('')
const captchaKey = ref('')

// 登录表单数据
const loginForm = reactive({
  account: '',
  password: '',
  checkCode: '',
  method: 'passwd',
  mail: ''
})

// 注册表单数据
const registerForm = reactive({
  name: '',
  account: '',
  passwd: '',
  confirmPasswd: '',
  sex: '男',
  avatarId: 1,
  avatarUrl: ''
})

// 登录表单验证规则
const loginRules = reactive<FormRules>({
  account: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 3, max: 20, message: '账号长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 4, max: 30, message: '密码长度在 4 到 30 个字符', trigger: 'blur' }
  ],
  checkCode: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
})

// 自定义确认密码验证
const validateConfirmPasswd = (rule: any, value: string, callback: any) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== registerForm.passwd) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

// 注册表单验证规则
const registerRules = reactive<FormRules>({
  name: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 2, max: 20, message: '昵称长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  account: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 3, max: 20, message: '账号长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  passwd: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 4, max: 30, message: '密码长度在 4 到 30 个字符', trigger: 'blur' }
  ],
  confirmPasswd: [
    { required: true, validator: validateConfirmPasswd, trigger: 'blur' }
  ],
  sex: [{ required: true, message: '请选择性别', trigger: 'change' }]
})

// 切换到登录
const switchToLogin = () => {
  isLogin.value = true
  if (isLogin.value) {
    fetchCaptcha()
  }
}

// 切换到注册
const switchToRegister = () => {
  isLogin.value = false
}

// 获取验证码
const fetchCaptcha = async () => {
  try {
    const res: any = await request.get('/sms/captcha/create')
    // API返回格式：{ key: string, img: base64字符串 }
    if (res && res.key && res.img) {
      captchaKey.value = res.key
      captchaImg.value = res.img.startsWith('data:image') ? res.img : `data:image/png;base64,${res.img}`
    }
  } catch (error) {
    ElMessage.error('获取验证码失败，请重试')
  }
}

// 登录处理
const handleLogin = async () => {
  if (!loginFormRef.value) return
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const payload = {
          account: loginForm.account,
          password: loginForm.password,
          mail: loginForm.mail,
          method: loginForm.method,
          checkCode: loginForm.checkCode,
          checkCodeKey: captchaKey.value
        }
        const res: any = await request.post('/auth/login', payload)
        // API返回格式：{ expires: number, token: string, token_type: string }
        if (res.token) {
          ElMessage.success('登录成功')
          localStorage.setItem('token', res.token)
          localStorage.setItem('token_type', res.token_type || 'Bearer')
          router.push('/')
        }
      } catch (error) {
        // 登录失败自动刷新验证码
        fetchCaptcha()
        loginForm.checkCode = ''
      } finally {
        loading.value = false
      }
    }
  })
}

// 注册处理
const handleRegister = async () => {
  if (!registerFormRef.value) return
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const payload = {
          name: registerForm.name,
          avatarId: registerForm.avatarId,
          avatarUrl: registerForm.avatarUrl || `https://api.dicebear.com/7.x/avataaars/svg?seed=${registerForm.account}`,
          sex: registerForm.sex,
          account: registerForm.account,
          passwd: registerForm.passwd
        }
        await request.post('/auth/user/register', payload)
        ElMessage.success('注册成功，请登录')
        // 注册成功后切换到登录页
        isLogin.value = true
        loginForm.account = registerForm.account
        loginForm.password = ''
        fetchCaptcha()
      } catch (error) {
        console.error('注册失败:', error)
      } finally {
        loading.value = false
      }
    }
  })
}

onMounted(() => {
  fetchCaptcha()
})
</script>

<style scoped>
/* 登录页背景光球动画 */
@keyframes blob {
  0% {
    transform: translate(0px, 0px) scale(1);
  }

  33% {
    transform: translate(30px, -50px) scale(1.1);
  }

  66% {
    transform: translate(-20px, 20px) scale(0.9);
  }

  100% {
    transform: translate(0px, 0px) scale(1);
  }
}

.animate-blob {
  animation: blob 7s infinite;
}

.animation-delay-2000 {
  animation-delay: 2s;
}

.animation-delay-4000 {
  animation-delay: 4s;
}
</style>
