import { ref } from 'vue'
import SparkMD5 from 'spark-md5'
import axios from 'axios'

export interface UploadOptions {
  file: File
  onProgress?: (progress: number) => void
  onMessage?: (message: string) => void
  chunkSize?: number // 分片大小，默认 5MB
}

// MediaVo 对象结构
export interface MediaVo {
  mediaId?: number
  mediaUrl?: string
  signature?: string
  state?: string
}

export interface UploadResult {
  success: boolean
  message: string
  fileMd5?: string
  mediaId?: number
  mediaUrl?: string
}

// 音频文件扩展名
const audioExtensions = ['mp3', 'wav', 'flac', 'aac', 'ogg', 'm4a', 'wma', 'ape']
// 视频文件扩展名
const videoExtensions = ['mp4', 'avi', 'mkv', 'mov', 'wmv', 'flv', 'webm', 'rmvb']

// 判断是否为音频或视频文件（需要分块上传）
const isMediaFile = (fileName: string): boolean => {
  const ext = fileName.split('.').pop()?.toLowerCase() || ''
  return audioExtensions.includes(ext) || videoExtensions.includes(ext)
}

// 计算文件MD5
const calculateFileMD5 = (file: File, onProgress?: (progress: number) => void): Promise<string> => {
  return new Promise((resolve, reject) => {
    const chunkSize = 2 * 1024 * 1024 // 2MB
    const chunks = Math.ceil(file.size / chunkSize)
    let currentChunk = 0
    const spark = new SparkMD5.ArrayBuffer()
    const fileReader = new FileReader()

    if (onProgress) onProgress(0)

    fileReader.onload = (e) => {
      spark.append(e.target?.result as ArrayBuffer)
      currentChunk++
      if (onProgress) onProgress(Math.floor((currentChunk / chunks) * 20)) // MD5计算占总进度 20%

      if (currentChunk < chunks) {
        loadNext()
      } else {
        const md5 = spark.end()
        resolve(md5)
      }
    }

    fileReader.onerror = () => reject(new Error('文件读取失败'))

    const loadNext = () => {
      const start = currentChunk * chunkSize
      const end = start + chunkSize >= file.size ? file.size : start + chunkSize
      fileReader.readAsArrayBuffer(file.slice(start, end))
    }

    loadNext()
  })
}

// 计算分片的MD5
const calculateChunkMD5 = async (chunk: Blob): Promise<string> => {
  const buffer = await chunk.arrayBuffer()
  return SparkMD5.ArrayBuffer.hash(buffer)
}

// 使用签证URL上传文件/分片（PUT请求）
const uploadToPresignedUrl = async (url: string, data: Blob | File): Promise<void> => {
  await fetch(url, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/octet-stream'
    },
    body: data
  })
}

// 延迟函数
const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms))

// 获取带认证的请求头
const getAuthHeaders = () => {
  const token = localStorage.getItem('token')
  const tokenType = localStorage.getItem('token_type') || 'Bearer'
  return {
    'Authorization': `${tokenType} ${token}`,
    'Content-Type': 'application/json'
  }
}

// 处理401未授权错误 - 跳转登录页
const handleUnauthorized = () => {
  // 清除本地存储的token
  localStorage.removeItem('token')
  localStorage.removeItem('token_type')

  // 跳转到登录页
  window.location.href = '/login'
}

// 检查响应是否为401错误
const checkUnauthorized = (error: any): boolean => {
  if (error.response && error.response.status === 401) {
    handleUnauthorized()
    return true
  }
  return false
}

// 检查文件是否存在（使用原始axios以获取完整响应）
// 后端返回格式: R<MediaVo>
//   秒传成功: { code: 200, msg: "ok", data: { mediaId, url } }
//   需要上传: { code: 600, msg: "待上传", data: { signature: 签证URL } }
//   busy:     { code: 600, msg: "busy" }
const checkFileExists = async (fileMd5: string, fileName: string): Promise<{
  status: 'ok' | 'busy' | 'signature';
  mediaId?: number;
  mediaUrl?: string;
  signatureUrl?: string
}> => {
  try {
    const response = await axios.get('/api/media/check/file', {
      params: { fileMd5, fileName },
      headers: getAuthHeaders(),
      timeout: 30000
    })

    const res = response.data
    const code = res.code
    const msg = res.msg
    const data = res.data as MediaVo | null

    // code 200 表示成功（秒传）
    if (code === 200 && data) {
      return {
        status: 'ok',
        mediaId: data.mediaId,
        mediaUrl: data.mediaUrl
      }
    }

    // code 600 表示需要处理
    if (code === 600) {
      if (msg === 'busy') {
        return { status: 'busy' }
      }

      // 其他情况：待上传，data中包含signature
      if (data && data.signature) {
        return { status: 'signature', signatureUrl: data.signature }
      }

      // 如果msg本身是URL
      if (msg && (msg.startsWith('http://') || msg.startsWith('https://'))) {
        return { status: 'signature', signatureUrl: msg }
      }
    }

    return { status: 'busy' }
  } catch (error: any) {
    // 检查401错误
    if (checkUnauthorized(error)) {
      throw new Error('未授权，请重新登录')
    }
    throw new Error('检查文件失败')
  }
}

// 检查分片是否存在（带重试机制）
// 后端返回格式: R<String>
//   已存在: { code: 200, msg: "ok", data: "ok" }
//   需要上传: { code: 600, msg: 签证URL }
//   busy:   { code: 600, msg: "busy" }
const checkChunkExistsWithRetry = async (
  id: number,
  chunkMd5: string,
  fileMd5: string,
  maxRetries: number = 3,
  onMessage?: (message: string) => void
): Promise<{ status: 'ok' | 'busy' | 'signature'; data?: string }> => {

  for (let retry = 0; retry < maxRetries; retry++) {
    try {
      const response = await axios.get('/api/media/check/chunk', {
        params: { id, chunkMd5, fileMd5 },
        headers: getAuthHeaders(),
        timeout: 30000
      })

      const res = response.data
      const code = res.code
      const msg = res.msg

      // code 200 表示成功（分片已存在）
      if (code === 200) {
        return { status: 'ok' }
      }

      // code 600 表示需要处理
      if (code === 600) {
        if (msg === 'busy') {
          // busy状态，等待1秒后重试
          if (retry < maxRetries - 1) {
            if (onMessage) onMessage(`分片 ${id + 1} 正在被处理，等待重试... (${retry + 1}/${maxRetries})`)
            await delay(1000)
            continue
          }
          return { status: 'busy' }
        }

        // msg是签证URL
        if (msg && (msg.startsWith('http://') || msg.startsWith('https://'))) {
          return { status: 'signature', data: msg }
        }

        return { status: 'signature', data: msg }
      }
    } catch (error: any) {
      // 检查401错误 - 如果是401直接跳转，不重试
      if (checkUnauthorized(error)) {
        throw new Error('未授权，请重新登录')
      }

      if (retry < maxRetries - 1) {
        await delay(1000)
        continue
      }
      throw new Error('检查分片失败')
    }
  }

  return { status: 'busy' }
}

// 合并分片
// 后端返回格式: R<MediaVo>
//   成功: { code: 200, msg: "ok", data: { mediaId, url } }
//   失败: { code: 600, msg: "错误信息" }
const mergeChunks = async (total: number, fileMd5: string, fileName: string): Promise<{ mediaId?: number; mediaUrl?: string }> => {
  try {
    const response = await axios.get('/api/media/merge', {
      params: { total, fileMd5, fileName },
      headers: getAuthHeaders(),
      timeout: 60000
    })

    const res = response.data
    const data = res.data as MediaVo | null

    if (res.code === 200 && data) {
      return {
        mediaId: data.mediaId,
        mediaUrl: data.mediaUrl
      }
    }

    throw new Error(res.msg || '合并失败')
  } catch (error: any) {
    // 检查401错误
    if (checkUnauthorized(error)) {
      throw new Error('未授权，请重新登录')
    }
    throw error
  }
}

// 主上传函数
export const useChunkUpload = () => {
  const isUploading = ref(false)

  const uploadFile = async (options: UploadOptions): Promise<UploadResult> => {
    const {
      file,
      onProgress,
      onMessage,
      chunkSize: customChunkSize = 5 * 1024 * 1024 // 默认5MB
    } = options

    isUploading.value = true

    try {
      if (onMessage) onMessage('正在计算文件 MD5...')

      // 1. 计算文件MD5
      const fileMd5 = await calculateFileMD5(file, onProgress)

      if (onMessage) onMessage('正在校验文件...')

      // 2. 检查文件是否存在（秒传）
      let fileCheck = await checkFileExists(fileMd5, file.name)

      // 如果是busy状态，重试3次，每次等待1秒
      for (let i = 0; i < 3; i++) {
        if (fileCheck.status === 'busy') {
          if (onMessage) onMessage(`文件正在被其他用户上传，等待重试... (${i + 1}/3)`)
          await delay(1000)
          fileCheck = await checkFileExists(fileMd5, file.name)
        } else {
          break
        }
      }

      // 文件已存在（秒传成功）
      if (fileCheck.status === 'ok') {
        if (onProgress) onProgress(100)
        return {
          success: true,
          message: '秒传成功！',
          fileMd5,
          mediaId: fileCheck.mediaId,
          mediaUrl: fileCheck.mediaUrl
        }
      }

      // 文件正在被别人上传
      if (fileCheck.status === 'busy') {
        return {
          success: false,
          message: '文件正在被其他用户上传，请稍后重试',
          fileMd5
        }
      }

      // 3. 判断是否需要分块上传
      // 如果是音频或视频文件，需要分块上传
      const needChunkUpload = isMediaFile(file.name)

      if (!needChunkUpload) {
        // 非媒体文件：直接上传到签证URL
        if (fileCheck.status === 'signature' && fileCheck.signatureUrl) {
          if (onMessage) onMessage('正在上传文件...')
          await uploadToPresignedUrl(fileCheck.signatureUrl, file)

          // 上传完成后，再次调用接口获取最终的mediaId和mediaUrl
          if (onMessage) onMessage('正在确认上传结果...')
          await delay(500)

          let retryCount = 0
          while (retryCount < 5) {
            const finalCheck = await checkFileExists(fileMd5, file.name)
            if (finalCheck.status === 'ok') {
              if (onProgress) onProgress(100)
              return {
                success: true,
                message: '上传成功！',
                fileMd5,
                mediaId: finalCheck.mediaId,
                mediaUrl: finalCheck.mediaUrl
              }
            }
            if (finalCheck.status === 'busy' || finalCheck.status === 'signature') {
              retryCount++
              if (onMessage) onMessage(`确认中... (${retryCount}/5)`)
              await delay(1000)
            } else {
              break
            }
          }

          if (onProgress) onProgress(100)
          return {
            success: true,
            message: '上传完成',
            fileMd5
          }
        }
      }

      // 4. 媒体文件分片上传流程
      const totalChunks = Math.ceil(file.size / customChunkSize)

      for (let i = 0; i < totalChunks; i++) {
        const start = i * customChunkSize
        const end = Math.min(start + customChunkSize, file.size)
        const chunk = file.slice(start, end)

        if (onMessage) onMessage(`正在处理分片 ${i + 1}/${totalChunks}...`)

        // 计算分片MD5
        const chunkMd5 = await calculateChunkMD5(chunk)

        // 检查分片是否已存在（带重试机制）
        const chunkCheck = await checkChunkExistsWithRetry(i, chunkMd5, fileMd5, 3, onMessage)

        if (chunkCheck.status === 'ok') {
          // 分片已存在，跳过
          if (onProgress) {
            onProgress(20 + Math.floor(((i + 1) / totalChunks) * 70))
          }
          continue
        }

        if (chunkCheck.status === 'busy') {
          return {
            success: false,
            message: `分片 ${i + 1} 上传失败，请稍后重试`,
            fileMd5
          }
        }

        // 上传分片到签证URL
        if (chunkCheck.status === 'signature' && chunkCheck.data) {
          if (onMessage) onMessage(`正在上传分片 ${i + 1}/${totalChunks}...`)
          await uploadToPresignedUrl(chunkCheck.data, chunk)

          // 更新进度
          if (onProgress) {
            onProgress(20 + Math.floor(((i + 1) / totalChunks) * 70))
          }
        }
      }

      // 5. 合并分片
      if (onMessage) onMessage('正在合并文件...')
      const mergeResult = await mergeChunks(totalChunks, fileMd5, file.name)

      // 如果合并接口返回了mediaId和mediaUrl，直接返回
      if (mergeResult.mediaId && mergeResult.mediaUrl) {
        if (onProgress) onProgress(100)
        return {
          success: true,
          message: '上传并合并成功！',
          fileMd5,
          mediaId: mergeResult.mediaId,
          mediaUrl: mergeResult.mediaUrl
        }
      }

      // 否则，再次调用checkFile接口获取最终结果
      if (onMessage) onMessage('正在确认上传结果...')
      await delay(500)

      let retryCount = 0
      while (retryCount < 10) {
        const finalCheck = await checkFileExists(fileMd5, file.name)
        if (finalCheck.status === 'ok') {
          if (onProgress) onProgress(100)
          return {
            success: true,
            message: '上传并合并成功！',
            fileMd5,
            mediaId: finalCheck.mediaId,
            mediaUrl: finalCheck.mediaUrl
          }
        }
        if (finalCheck.status === 'busy' || finalCheck.status === 'signature') {
          retryCount++
          if (onMessage) onMessage(`确认中... (${retryCount}/10)`)
          await delay(1000)
        } else {
          break
        }
      }

      if (onProgress) onProgress(100)
      return {
        success: true,
        message: '上传完成',
        fileMd5
      }

    } catch (error: any) {
      return {
        success: false,
        message: `上传失败: ${error.message || '未知错误'}`
      }
    } finally {
      isUploading.value = false
    }
  }

  return {
    isUploading,
    uploadFile
  }
}
