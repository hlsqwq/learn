import axios, {
  AxiosInstance,
  InternalAxiosRequestConfig,
  AxiosResponse,
} from "axios";
import { ElMessage } from "element-plus";
import router from "@/router";

// 基础服务路由说明 (通过代理 /api 访问)
// 认证服务: /auth
// 内容服务: /content
// 媒体上传: /media
// 短信验证码: /sms
// 搜索服务: /search

// 创建 axios 实例
const service: AxiosInstance = axios.create({
  baseURL: "http://localhost:81/api", // 匹配 vite.config.ts 中的 proxy 规则
  timeout: 30000,
  headers: {
    "Content-Type": "application/json;charset=utf-8",
  },
});

// 请求拦截器
service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // 从 localStorage 中获取 token 并添加
    const token = localStorage.getItem("token");
    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
  },
  (error: any) => {
    return Promise.reject(error);
  },
);

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    // 直接返回数据结构
    const res = response.data;
    // 如果是验证码等非标准包裹的数据或文件流，直接返回
    if (
      res instanceof Blob ||
      typeof res === "boolean" ||
      !res.hasOwnProperty("code")
    ) {
      return res;
    }
    // 处理通用响应结构
    if (res.code === 200 || res.code === 0) {
      return res.data !== undefined ? res.data : res;
    } else {
      ElMessage.error(res.msg || res.message || "请求失败");
      return Promise.reject(new Error(res.msg || res.message || "Error"));
    }
  },
  (error: any) => {
    let msg = "服务器异常，请稍后再试";
    if (error.response) {
      switch (error.response.status) {
        case 401:
          msg = "登录已过期，请重新登录";
          localStorage.removeItem("token");
          router.push("/login");
          break;
        case 403:
          msg = "无权限访问";
          break;
        case 404:
          msg = "请求的资源不存在";
          break;
        case 500:
          msg = "服务器内部错误";
          break;
        default:
          msg = `请求错误 ${error.response.status}`;
      }
    }
    ElMessage.error(msg);
    return Promise.reject(error);
  },
);

export default service;
