<template>
  <div class="h-full flex flex-col space-y-6">
    <!-- 顶部操作栏 -->
    <div
      class="flex justify-between items-center bg-white p-6 rounded-2xl shadow-[0_2px_12px_rgba(0,0,0,0.02)] border border-gray-50/50">
      <div class="flex space-x-4">
        <el-input v-model="searchQuery" placeholder="搜索歌曲..." :prefix-icon="Search" class="!w-72 !rounded-xl" clearable
          @clear="fetchSongs" @keyup.enter="fetchSongs" />
        <el-button type="primary" class="!rounded-xl shadow-md" @click="fetchSongs">
          搜索
        </el-button>
      </div>
      <el-button type="primary" :icon="Plus" class="!rounded-xl shadow-md font-bold" @click="showUploadDialog = true">
        上传歌曲
      </el-button>
    </div>

    <!-- 歌曲列表卡片网格展示 (非传统表格) -->
    <div class="flex-1 overflow-auto">
      <div v-if="loading" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
        <el-skeleton v-for="i in 8" :key="i" animated class="bg-white p-4 rounded-2xl h-32 shadow-sm" />
      </div>
      <div v-else-if="songList.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
        <div v-for="song in songList" :key="song.id"
          class="bg-white rounded-2xl p-5 flex items-center shadow-[0_4px_20px_rgba(0,0,0,0.03)] hover:shadow-[0_8px_30px_rgba(0,0,0,0.08)] transition-all group border border-gray-50 cursor-pointer relative overflow-hidden">
          <!-- 封面图 -->
          <div class="w-20 h-20 rounded-xl overflow-hidden shadow-md flex-shrink-0 bg-gray-100">
            <el-image :src="song.pic || '/default-album.png'" fit="cover" class="w-full h-full" />
          </div>

          <div class="ml-4 flex-1 min-w-0">
            <h3 class="text-lg font-bold text-gray-800 truncate">{{ song.name }}</h3>
            <p class="text-sm text-gray-400 truncate mt-1">{{ song.singerName || '未知歌手' }}</p>
            <div class="mt-2 flex space-x-2">
              <el-tag size="small" type="info" class="!rounded-lg border-none">{{ song.style || 'Pop' }}</el-tag>
            </div>
          </div>

          <!-- 悬浮操作按钮 -->
          <div
            class="absolute right-4 top-1/2 -translate-y-1/2 opacity-0 group-hover:opacity-100 transition-opacity flex space-x-2 bg-white/90 backdrop-blur-sm p-2 rounded-xl shadow-sm">
            <el-button circle size="small" type="primary" :icon="Edit" @click.stop="handleEdit(song)" />
            <el-popconfirm title="确定删除这首歌曲吗？" @confirm="handleDelete(song.id)">
              <template #reference>
                <el-button circle size="small" type="danger" :icon="Delete" @click.stop />
              </template>
            </el-popconfirm>
          </div>
        </div>
      </div>
      <el-empty v-else description="暂无歌曲数据" class="bg-white rounded-2xl h-full flex justify-center items-center" />
    </div>

    <!-- 分页 -->
    <div class="flex justify-end bg-white p-4 rounded-2xl shadow-sm">
      <el-pagination v-model:current-page="pageParams.num" v-model:page-size="pageParams.size" :total="total" background
        layout="total, prev, pager, next, jumper" class="!font-bold" @current-change="fetchSongs" />
    </div>

    <!-- 分片上传弹窗 -->
    <el-dialog v-model="showUploadDialog" title="上传歌曲文件" width="600px" class="!rounded-3xl custom-dialog"
      :close-on-click-modal="false" destroy-on-close>
      <div class="p-4 flex flex-col items-center">
        <!-- 拖拽上传区 -->
        <el-upload class="w-full" drag action="#" :auto-upload="false" :show-file-list="false"
          :on-change="handleFileChange">
          <div
            class="p-10 border-2 border-dashed border-gray-200 rounded-2xl hover:border-blue-500 hover:bg-blue-50/50 transition-colors">
            <el-icon class="el-icon--upload text-5xl text-blue-400 mb-4"><upload-filled /></el-icon>
            <div class="el-upload__text text-lg font-bold text-gray-700">
              将文件拖到此处，或 <em class="text-blue-600 not-italic">点击上传</em>
            </div>
            <div class="el-upload__tip text-gray-400 mt-2">支持 mp3/flac 格式，且不超过 500MB</div>
          </div>
        </el-upload>

        <!-- 上传进度 -->
        <div v-if="selectedFile" class="w-full mt-8 bg-gray-50 p-6 rounded-2xl border border-gray-100">
          <div class="flex justify-between items-center mb-3">
            <span class="font-bold text-gray-700 truncate mr-4">{{ selectedFile.name }}</span>
            <span class="text-sm text-gray-400 flex-shrink-0">{{ formatSize(selectedFile.size) }}</span>
          </div>
          <el-progress :percentage="uploadProgress" :status="uploadStatus" :stroke-width="12" striped
            class="custom-progress" />
          <div class="mt-4 text-center text-sm font-semibold" :class="statusTextColor">
            {{ uploadMessage }}
          </div>
        </div>
      </div>

      <template #footer>
        <span class="dialog-footer flex justify-end space-x-3">
          <el-button @click="showUploadDialog = false" class="!rounded-xl !px-6">取消</el-button>
          <el-button type="primary" @click="startUpload" :loading="isUploading" class="!rounded-xl !px-6 font-bold"
            :disabled="!selectedFile">
            开始上传
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { Search, Plus, Edit, Delete, UploadFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import type { UploadFile } from 'element-plus'
import request from '@/utils/request'
import { useChunkUpload } from '@/composables/useChunkUpload'

// 状态定义
const loading = ref(false)
const songList = ref<any[]>([])
const total = ref(0)
const searchQuery = ref('')
const pageParams = ref({ num: 1, size: 12 })

// 上传状态
const showUploadDialog = ref(false)
const selectedFile = ref<File | null>(null)
const uploadProgress = ref(0)
const isUploading = ref(false)
const uploadStatus = ref<'' | 'success' | 'warning' | 'exception'>('')
const uploadMessage = ref('等待上传...')

// 使用分片上传composable
const { isUploading: chunkUploading, uploadFile } = useChunkUpload()

// 计算状态文字颜色
const statusTextColor = computed(() => {
  if (uploadStatus.value === 'success') return 'text-green-500'
  if (uploadStatus.value === 'exception') return 'text-red-500'
  if (isUploading.value) return 'text-blue-500'
  return 'text-gray-400'
})

// 获取歌曲列表
const fetchSongs = async () => {
  loading.value = true
  try {
    // 接口文档：GET /content/song/page/{id} (此处假设 {id} 为分类或统一传1)
    const res: any = await request.get(`/content/song/page/1`, {
      params: {
        num: pageParams.value.num,
        size: pageParams.value.size,
        keyword: searchQuery.value
      }
    })
    // 根据通用分页结构解析
    songList.value = res.item || res.records || []
    total.value = res.total || 0
  } catch (error) {
    // 生产环境需处理错误
  } finally {
    loading.value = false
  }
}

// 删除歌曲
const handleDelete = async (id: number) => {
  try {
    await request.delete(`/content/song/delete/${id}`)
    ElMessage.success('删除成功')
    fetchSongs()
  } catch (error) {
    //
  }
}

// 编辑占位
const handleEdit = (song: any) => {
  ElMessage.info('编辑功能开发中')
}

// 格式化文件大小
const formatSize = (bytes: number) => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 文件选择
const handleFileChange = (uploadFile: UploadFile) => {
  if (uploadFile.raw) {
    selectedFile.value = uploadFile.raw
    uploadProgress.value = 0
    uploadStatus.value = ''
    uploadMessage.value = '文件已选择，等待上传'
  }
}

// 执行分片上传流程（使用签证模式）
const startUpload = async () => {
  if (!selectedFile.value) return
  isUploading.value = true
  uploadStatus.value = ''

  try {
    const result = await uploadFile({
      file: selectedFile.value,
      onProgress: (progress) => {
        uploadProgress.value = progress
      },
      onMessage: (message) => {
        uploadMessage.value = message
      }
    })

    if (result.success) {
      uploadProgress.value = 100
      uploadStatus.value = 'success'
      uploadMessage.value = result.message
      ElMessage.success(result.message)

      // 刷新列表
      fetchSongs()

      // 延迟关闭弹窗
      setTimeout(() => {
        showUploadDialog.value = false
        selectedFile.value = null
      }, 1500)
    } else {
      uploadStatus.value = 'exception'
      uploadMessage.value = result.message
      ElMessage.error(result.message)
    }

  } catch (error: any) {
    uploadStatus.value = 'exception'
    uploadMessage.value = `上传失败: ${error.message || '未知错误'}`
    ElMessage.error('上传失败')
  } finally {
    isUploading.value = false
  }
}

onMounted(() => {
  fetchSongs()
})
</script>

<style>
/* 覆盖 Element Plus 样式以符合圆角规范 */
.custom-dialog .el-dialog__header {
  padding: 24px;
  margin-right: 0;
  border-bottom: 1px solid #f3f4f6;
}

.custom-dialog .el-dialog__title {
  font-weight: 800;
  font-size: 1.25rem;
}

.custom-dialog .el-dialog__body {
  padding: 0;
}

.custom-dialog .el-dialog__footer {
  padding: 24px;
  border-top: 1px solid #f3f4f6;
}

.custom-progress .el-progress-bar__outer {
  border-radius: 100px;
  background-color: #f3f4f6;
}

.custom-progress .el-progress-bar__inner {
  border-radius: 100px;
}
</style>
