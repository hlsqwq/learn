import request from '@/utils/request'

// 歌手DTO类型
export interface SingerDto {
  name: string
  categoryId: number
  avatarId?: string // 歌手头像ID（Media ID）
  avatarUrl?: string // 歌手头像URL
  introduction: string
}

// 编辑歌手DTO（包含id）
export interface EditSingerDto extends SingerDto {
  id: number
}

// 歌手实体（从后端返回）
export interface Singer {
  id: number
  name: string
  categoryId: number
  avatarId?: string // 头像ID
  avatarUrl?: string // 头像URL
  avatar?: string // 兼容旧字段，用于显示
  introduction: string
  createTime?: string
  updateTime?: string
  fansCount?: number // 粉丝数（扩展字段）
}

// 分类树DTO
export interface CategoryTreeDto {
  id: number
  path: string
  content: string
  children?: CategoryTreeDto[]
}

// 获取所有分类列表
export const getCategoryList = () => {
  return request.get<any, CategoryTreeDto[]>('/content/category/list')
}

// 获取歌手信息
export const getSingerById = (id: number) => {
  return request.get<any, Singer>(`/content/singer/${id}`)
}

// 添加歌手
export const addSinger = (data: SingerDto) => {
  return request.post('/content/singer/add', data)
}

// 更新歌手
export const updateSinger = (data: EditSingerDto) => {
  return request.put('/content/singer/update', data)
}

// 删除歌手
export const deleteSinger = (id: number) => {
  return request.delete(`/content/singer/${id}`)
}

// 获取top10歌手
export const getTop10Singers = (categoryId: number = 1) => {
  return request.get<any, any>('/content/singer/top10', {
    params: { id: categoryId }
  })
}

// 获取歌手粉丝数
export const getSingerFans = (singerId: number) => {
  return request.get<any, { fansCount: number }>(`/content/singer/fans/${singerId}`)
}

// 关注歌手
export const followSinger = (singerId: number) => {
  return request.post<any, { fansCount: number }>(`/content/singer/follow/${singerId}`)
}

// 取消关注歌手
export const unfollowSinger = (singerId: number) => {
  return request.post<any, { fansCount: number }>(`/content/singer/unfollow/${singerId}`)
}
