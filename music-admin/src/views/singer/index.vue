<template>
  <div class="h-full flex flex-col space-y-6">
    <!-- 顶部操作栏 -->
    <div
      class="flex justify-between items-center bg-white p-6 rounded-2xl shadow-[0_2px_12px_rgba(0,0,0,0.02)] border border-gray-50/50">
      <div class="flex space-x-4">
        <el-input v-model="searchQuery" placeholder="搜索歌手..." :prefix-icon="Search" class="!w-72 !rounded-xl" clearable
          @clear="fetchSingers" @keyup.enter="fetchSingers" />
        <el-button type="primary" class="!rounded-xl shadow-md" @click="fetchSingers">
          搜索
        </el-button>
      </div>
      <el-button type="primary" :icon="Plus" class="!rounded-xl shadow-md font-bold" @click="handleAdd">
        添加歌手
      </el-button>
    </div>

    <!-- 歌手列表 -->
    <div class="flex-1 overflow-auto">
      <div v-if="loading" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
        <el-skeleton v-for="i in 8" :key="i" animated class="bg-white p-6 rounded-2xl h-64 shadow-sm" />
      </div>

      <div v-else-if="singerList.length > 0"
        class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
        <div v-for="singer in singerList" :key="singer.id"
          class="bg-white rounded-2xl p-6 flex flex-col items-center shadow-[0_4px_20px_rgba(0,0,0,0.03)] hover:shadow-[0_8px_30px_rgba(0,0,0,0.08)] transition-all group border border-gray-50 relative overflow-hidden">
          <!-- 头像 -->
          <div
            class="w-24 h-24 rounded-full overflow-hidden shadow-lg mb-4 ring-4 ring-blue-100 group-hover:ring-blue-300 transition-all">
            <el-image :src="getFullFileUrl(singer.avatarUrl || singer.avatar) || '/default-avatar.png'" fit="cover"
              class="w-full h-full">
              <template #error>
                <div
                  class="w-full h-full bg-gradient-to-br from-blue-400 to-purple-500 flex items-center justify-center text-white text-3xl font-bold">
                  {{ singer.name?.charAt(0) }}
                </div>
              </template>
            </el-image>
          </div>

          <!-- 歌手信息 -->
          <h3 class="text-lg font-bold text-gray-800 truncate w-full text-center">{{ singer.name }}</h3>

          <!-- 分类标签 -->
          <el-tag size="small" type="info" class="!rounded-lg mt-2 border-none">
            {{ getCategoryName(singer.categoryId) }}
          </el-tag>

          <!-- 简介 -->
          <p class="text-xs text-gray-400 mt-3 line-clamp-2 text-center px-2 min-h-[32px]">
            {{ singer.introduction || '暂无简介' }}
          </p>

          <!-- 粉丝数和关注按钮 -->
          <div class="flex items-center space-x-3 mt-4 w-full justify-center">
            <div class="flex items-center space-x-1 text-gray-500 text-sm">
              <el-icon>
                <User />
              </el-icon>
              <span>{{ singer.fansCount || 0 }} 粉丝</span>
            </div>

            <el-button :type="singer.isFollowed ? 'default' : 'primary'" size="small" class="!rounded-lg"
              @click.stop="handleFollow(singer)">
              {{ singer.isFollowed ? '已关注' : '+ 关注' }}
            </el-button>
          </div>

          <!-- 悬浮操作按钮 -->
          <div
            class="absolute right-4 top-4 opacity-0 group-hover:opacity-100 transition-opacity flex space-x-2 bg-white/90 backdrop-blur-sm p-2 rounded-xl shadow-sm">
            <el-tooltip content="编辑" placement="top">
              <el-button circle size="small" type="primary" :icon="Edit" @click.stop="handleEdit(singer)" />
            </el-tooltip>
            <el-popconfirm title="确定删除该歌手吗？" @confirm="handleDelete(singer.id)">
              <template #reference>
                <el-button circle size="small" type="danger" :icon="Delete" @click.stop />
              </template>
            </el-popconfirm>
          </div>
        </div>
      </div>

      <el-empty v-else description="暂无歌手数据" class="bg-white rounded-2xl h-full flex justify-center items-center" />
    </div>

    <!-- 分页 -->
    <div v-if="total > 0" class="flex justify-end bg-white p-4 rounded-2xl shadow-sm">
      <el-pagination v-model:current-page="pageParams.num" v-model:page-size="pageParams.size" :total="total" background
        layout="total, prev, pager, next, jumper" class="!font-bold" @current-change="fetchSingers" />
    </div>

    <!-- 添加/编辑歌手弹窗 -->
    <el-dialog v-model="showDialog" :title="isEdit ? '编辑歌手' : '添加歌手'" width="600px" class="!rounded-3xl custom-dialog"
      :close-on-click-modal="false" destroy-on-close>
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="80px" class="mt-6">
        <el-form-item label="歌手姓名" prop="name">
          <el-input v-model="formData.name" placeholder="请输入歌手姓名" class="!rounded-xl" clearable />
        </el-form-item>

        <el-form-item label="所属分类" prop="categoryId">
          <el-select v-model="formData.categoryId" placeholder="请选择分类" class="!w-full !rounded-xl">
            <el-option v-for="category in flatCategoryList" :key="category.id" :label="category.content"
              :value="category.id" />
          </el-select>
        </el-form-item>

        <el-form-item label="歌手头像" prop="avatarUrl">
          <div class="flex items-start space-x-4">
            <el-upload class="avatar-uploader" action="#" :auto-upload="false" :show-file-list="false"
              :on-change="handleAvatarChange" accept="image/*">
              <div v-if="avatarPreview || formData.avatarUrl"
                class="relative w-24 h-24 rounded-xl overflow-hidden shadow-md">
                <img :src="getFullFileUrl(avatarPreview || formData.avatarUrl)" alt="头像"
                  class="w-full h-full object-cover" />
                <div
                  class="absolute inset-0 bg-black/40 opacity-0 hover:opacity-100 transition-opacity flex items-center justify-center cursor-pointer">
                  <el-icon class="text-white text-xl">
                    <Refresh />
                  </el-icon>
                </div>
              </div>
              <div v-else
                class="w-24 h-24 rounded-xl border-2 border-dashed border-gray-200 hover:border-blue-400 flex flex-col items-center justify-center cursor-pointer transition-colors bg-gray-50 hover:bg-blue-50">
                <el-icon class="text-3xl text-gray-400">
                  <Plus />
                </el-icon>
                <span class="text-xs text-gray-400 mt-1">上传</span>
              </div>
            </el-upload>

            <div class="flex-1 text-sm text-gray-500">
              <p>支持 JPG、PNG 格式</p>
              <p>建议尺寸：200x200 像素</p>
              <p>文件大小不超过 2MB</p>
            </div>
          </div>
        </el-form-item>

        <el-form-item label="歌手简介" prop="introduction">
          <el-input v-model="formData.introduction" type="textarea" :rows="4" placeholder="请输入歌手简介..." maxlength="500"
            show-word-limit class="!rounded-xl" />
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer flex justify-end space-x-3">
          <el-button @click="showDialog = false" class="!rounded-xl !px-6">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitting" class="!rounded-xl !px-6 font-bold">
            {{ isEdit ? '保存' : '添加' }}
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { Search, Plus, Edit, Delete, User, Refresh } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules, UploadFile } from 'element-plus'
import * as singerApi from '@/api/singer'
import { useChunkUpload } from '@/composables/useChunkUpload'
import type { Singer, CategoryTreeDto, EditSingerDto, SingerDto } from '@/api/singer'

// 状态定义
const loading = ref(false)
const submitting = ref(false)
const singerList = ref<(Singer & { isFollowed?: boolean })[]>([])
const total = ref(0)
const searchQuery = ref('')
const pageParams = ref({ num: 1, size: 12 })

// 分类列表（树形结构）
const categoryList = ref<CategoryTreeDto[]>([])

// 扁平化的分类列表（用于下拉选择，只显示叶子节点/二级分类）
const flatCategoryList = computed<CategoryTreeDto[]>(() => {
  const flatten = (categories: CategoryTreeDto[]): CategoryTreeDto[] => {
    const result: CategoryTreeDto[] = []
    for (const cat of categories) {
      // 如果有子节点，只取子节点（一级分类不加入选择）
      if (cat.children && cat.children.length > 0) {
        result.push(...cat.children)
      } else {
        // 没有子节点的直接添加
        result.push(cat)
      }
    }
    return result
  }
  return flatten(categoryList.value)
})

// 弹窗状态
const showDialog = ref(false)
const isEdit = ref(false)
const formRef = ref<FormInstance>()

// 表单数据
const formData = reactive<EditSingerDto>({
  id: 0,
  name: '',
  categoryId: 1,
  avatarId: '',
  avatarUrl: '',
  introduction: ''
})

// 头像上传文件引用
const avatarFile = ref<File | null>(null)
// 本地预览URL（用于显示）
const avatarPreview = ref('')

// 使用分片上传composable
const { isUploading: avatarUploading, uploadFile: uploadMedia } = useChunkUpload()

// 文件服务器基础地址
const FILE_SERVER_BASE_URL = 'http://192.168.124.8:9000'

// 获取完整的文件访问URL（拼接服务器地址）
const getFullFileUrl = (url: string): string => {
  if (!url) return ''
  // 如果已经是完整URL，直接返回
  if (url.startsWith('http://') || url.startsWith('https://')) {
    return url
  }
  // 否则拼接文件服务器地址
  return `${FILE_SERVER_BASE_URL}/${url.startsWith('/') ? url.slice(1) : url}`
}

// 表单验证规则
const formRules = reactive<FormRules>({
  name: [
    { required: true, message: '请输入歌手姓名', trigger: 'blur' },
    { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择分类', trigger: 'change' }
  ],
  introduction: [
    { max: 500, message: '简介不能超过500个字符', trigger: 'blur' }
  ]
})

// 获取分类名称（使用content字段）
const getCategoryName = (categoryId: number): string => {
  const findCategory = (categories: CategoryTreeDto[], id: number): string => {
    for (const cat of categories) {
      if (cat.id === id) return cat.content || '未分类'
      if (cat.children) {
        const found = findCategory(cat.children, id)
        if (found) return found
      }
    }
    return '未分类'
  }
  return findCategory(categoryList.value, categoryId)
}

// 获取分类列表
const fetchCategories = async () => {
  try {
    const res = await singerApi.getCategoryList()
    categoryList.value = res || []
  } catch (error) {
    console.error('获取分类失败:', error)
  }
}

// 获取歌手列表
const fetchSingers = async () => {
  loading.value = true
  try {
    const res = await singerApi.getTop10Singers(1)
    // 根据实际返回格式调整
    if (Array.isArray(res)) {
      singerList.value = res.map(item => ({
        ...item,
        isFollowed: false
      }))
    } else if (res && Array.isArray(res.item)) {
      singerList.value = res.item.map((item: any) => ({
        ...item,
        isFollowed: false
      }))
      total.value = res.total || 0
    }
  } catch (error) {
    console.error('获取歌手列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 处理添加
const handleAdd = () => {
  isEdit.value = false
  Object.assign(formData, {
    id: 0,
    name: '',
    categoryId: flatCategoryList.value[0]?.id || 1,
    avatarId: '',
    avatarUrl: '',
    introduction: ''
  })
  avatarFile.value = null
  avatarPreview.value = ''
  showDialog.value = true
}

// 处理编辑
const handleEdit = (singer: Singer) => {
  isEdit.value = true
  Object.assign(formData, {
    id: singer.id,
    name: singer.name,
    categoryId: singer.categoryId,
    avatarId: singer.avatarId || '',
    avatarUrl: singer.avatarUrl || singer.avatar || '',
    introduction: singer.introduction
  })
  avatarFile.value = null
  // 使用avatarUrl或avatar显示图片
  avatarPreview.value = singer.avatarUrl || singer.avatar || ''
  showDialog.value = true
}

// 处理删除
const handleDelete = async (id: number) => {
  try {
    await singerApi.deleteSinger(id)
    ElMessage.success('删除成功')
    fetchSingers()
  } catch (error) {
    ElMessage.error('删除失败')
  }
}


// 头像上传处理（选择文件后立即上传到服务器）
const handleAvatarChange = async (uploadFile: UploadFile) => {
  if (uploadFile.raw) {
    const file = uploadFile.raw

    // 验证文件类型 - 只允许图片，不允许视频和音频
    const isImage = file.type.startsWith('image/')
    const isVideo = file.type.startsWith('video/')
    const isAudio = file.type.startsWith('audio/')

    if (!isImage) {
      ElMessage.error('只能上传图片文件!')
      return
    }

    if (isVideo || isAudio) {
      ElMessage.error('不支持上传视频或音频文件!')
      return
    }

    const isLt2M = file.size / 1024 / 1024 < 2

    if (!isLt2M) {
      ElMessage.error('图片大小不能超过 2MB!')
      return
    }

    // 保存文件引用
    avatarFile.value = file

    try {
      // 立即上传到Media服务获取URL
      ElMessage.info('正在上传头像...')

      const result = await uploadMedia({
        file: file,
        onMessage: (msg) => {
          ElMessage.info(msg)
        }
      })


      if (result.success && result.mediaId && result.mediaUrl) {
        // 上传成功，使用服务器返回的URL显示
        avatarPreview.value = result.mediaUrl
        formData.avatarId = String(result.mediaId)
        formData.avatarUrl = result.mediaUrl

        ElMessage.success('头像上传成功')
      } else {
        throw new Error(result.message || '头像上传失败')
      }
    } catch (error: any) {
      ElMessage.error(error.message || '头像上传失败')
      avatarPreview.value = ''
      formData.avatarId = ''
      formData.avatarUrl = ''
      avatarFile.value = null
    }
  }
}

// 表单提交（头像已在选择时上传完成）
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return

    submitting.value = true
    try {
      if (isEdit.value) {
        // 更新歌手
        await singerApi.updateSinger({
          id: formData.id,
          name: formData.name,
          categoryId: formData.categoryId,
          avatarId: formData.avatarId,
          avatarUrl: formData.avatarUrl,
          introduction: formData.introduction
        })
        ElMessage.success('更新成功')
      } else {
        // 添加歌手（调用 /content/singer/add）
        const data: SingerDto = {
          name: formData.name,
          categoryId: formData.categoryId,
          avatarId: formData.avatarId,
          avatarUrl: formData.avatarUrl,
          introduction: formData.introduction
        }
        await singerApi.addSinger(data)
        ElMessage.success('添加成功')
      }

      showDialog.value = false
      fetchSingers()
    } catch (error: any) {
      ElMessage.error(error.message || '操作失败')
    } finally {
      submitting.value = false
    }
  })
}

// 关注/取消关注
const handleFollow = async (singer: Singer & { isFollowed?: boolean }) => {
  try {
    let res: any
    if (singer.isFollowed) {
      res = await singerApi.unfollowSinger(singer.id)
      singer.isFollowed = false
      ElMessage.success('已取消关注')
    } else {
      res = await singerApi.followSinger(singer.id)
      singer.isFollowed = true
      ElMessage.success('关注成功')
    }

    // 更新粉丝数
    if (res && typeof res.fansCount !== 'undefined') {
      singer.fansCount = res.fansCount
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

onMounted(() => {
  fetchCategories()
  fetchSingers()
})
</script>

<style>
/* 弹窗样式 */
.custom-dialog .el-dialog__header {
  padding: 20px 24px;
  margin-right: 0;
  border-bottom: 1px solid #f3f4f6;
}

.custom-dialog .el-dialog__title {
  font-weight: 800;
  font-size: 1.25rem;
}

.custom-dialog .el-dialog__body {
  padding: 30px 24px;
}

.custom-dialog .el-dialog__footer {
  padding: 16px 24px;
  border-top: 1px solid #f3f4f6;
}

/* 头像上传样式 */
.avatar-uploader .el-upload {
  border: none;
}

.avatar-uploader:hover {
  border-color: transparent;
}

/* 文本截断 */
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
