// API 服务

const API_BASE_URL = '/api'

// 通用请求方法
const request = async (url, options = {}) => {
  const token = localStorage.getItem('token')
  const headers = {
    'Content-Type': 'application/json',
    ...options.headers
  }
  if (token) {
    headers['Authorization'] = `Bearer ${token}`
  }
  try {
    const response = await fetch(`${API_BASE_URL}${url}`, {
      ...options,
      headers
    })
    
    const contentType = response.headers.get('content-type')
    if (contentType && contentType.includes('application/json')) {
      const data = await response.json()
      if (!response.ok) {
        throw new Error(data.msg || data.message || 'Request failed')
      }
      return data
    } else {
      if (!response.ok) {
        throw new Error('Network response was not ok')
      }
      return await response.text()
    }
  } catch (error) {
    console.error('API request failed:', error)
    throw error
  }
}

export const api = {
  // 验证码接口
  getCaptcha: (uuid, code) => request(`/sms/captcha/validate?uuid=${uuid}&code=${code}`),
  createCaptcha: () => request('/sms/captcha/create'),

  // 认证相关
  login: (data) => request('/auth/login', {
    method: 'POST',
    body: JSON.stringify(data)
  }),

  // 用户相关
  getUserList: (ids) => request(`/auth/user/list?ids=${ids.join(',')}`),

  // 搜索相关
  searchAll: (keyword) => request(`/search/search/all?keyword=${keyword}`),
  searchByType: (type, keyword, num, size, order) => {
    let url = `/search/search/${type}?keyword=${keyword}&pageParam.num=${num}&pageParam.size=${size}`
    if (order) url += `&order=${order}`
    return request(url)
  },

  // 歌曲/歌单/歌手更新相关
  updateSong: (data) => request('/content/song/update', { method: 'PUT', body: JSON.stringify(data) }),
  updateSongList: (data) => request('/content/song-list/update', { method: 'PUT', body: JSON.stringify(data) }),
  updateSinger: (data) => request('/content/singer/update', { method: 'PUT', body: JSON.stringify(data) }),
  updateMv: (data) => request('/content/mv/update', { method: 'PUT', body: JSON.stringify(data) }),
  updateCategory: (id, content) => request(`/content/category/update?id=${id}&content=${content}`, { method: 'PUT' }),

  // 媒体处理相关
  mergeMedia: (total, fileMd5, fileName) => request(`/media/merge?total=${total}&fileMd5=${fileMd5}&fileName=${fileName}`),
  checkFile: (fileMd5, fileName) => request(`/media/check/file?fileMd5=${fileMd5}&fileName=${fileName}`),
  checkChunk: (id, chunkMd5, fileMd5) => request(`/media/check/chunk?id=${id}&chunkMd5=${chunkMd5}&fileMd5=${fileMd5}`),
  deleteUserMedia: (id) => request(`/media/user-media/del/${id}`, { method: 'DELETE' })
}

// 模拟数据
const getMockData = (url, options) => {
  // 认证相关
  if (url === '/auth/login' && options.method === 'POST') {
    return {
      code: 0,
      message: '登录成功',
      expires: Date.now() + 3600000,
      token: 'mock-token-123',
      token_type: 'Bearer'
    }
  }

  // 用户相关
  if (url === '/auth/user/list') {
    return [
      {
        id: 1,
        name: '用户 A',
        avatarId: 1,
        avatarUrl: 'https://picsum.photos/200/200?random=1',
        sex: 'male',
        account: 'user@example.com',
        passwd: '',
        followNum: 100,
        fansNum: 500,
        songListId: 1,
        access: 'user',
        createTime: new Date().toISOString()
      }
    ]
  }

  // 获取歌单详情
  if (url.startsWith('/content/song-list/detail/')) {
    return {
      id: 1,
      name: '流行热歌',
      userId: 1,
      avatar: 'https://picsum.photos/400/400?random=1',
      playNum: 10000,
      likeNum: 5000,
      introduction: '流行热歌合集',
      createTime: new Date().toISOString(),
      songList: [
        {
          id: 1,
          singerId: 1,
          userId: 1,
          music: 'https://example.com/music/1.mp3',
          name: '歌曲 1',
          avatar: 'https://picsum.photos/200/200?random=1',
          album: '专辑 1',
          duration: '00:03:30',
          likeNum: 1000,
          playNum: 5000,
          commentNum: 200,
          introduction: '歌曲 1 简介',
          status: 'normal',
          createTime: new Date().toISOString()
        }
      ]
    }
  }

  // 获取热门歌手
  if (url === '/content/singer/top10') {
    return [
      {
        name: '周杰伦',
        avatar: 'https://picsum.photos/200/200?random=10',
        introduction: '华语流行天王',
        createTime: new Date().toISOString(),
        id: 1
      },
      {
        name: '陈奕迅',
        avatar: 'https://picsum.photos/200/200?random=11',
        introduction: '情歌王子',
        createTime: new Date().toISOString(),
        id: 2
      },
      {
        name: 'Taylor Swift',
        avatar: 'https://picsum.photos/200/200?random=12',
        introduction: '流行天后',
        createTime: new Date().toISOString(),
        id: 3
      }
    ]
  }

  // 获取 MV 列表
  if (url === '/content/mv/list') {
    return [
      {
        id: 1,
        singerId: 1,
        name: '告白气球',
        video: 'https://example.com/mv/1.mp4',
        avatar: 'https://picsum.photos/400/400?random=20',
        status: 'normal',
        playNum: 10000,
        commentNum: 500,
        likeNum: 2000,
        userId: 1,
        createTime: new Date().toISOString()
      },
      {
        id: 2,
        singerId: 1,
        name: '反方向的钟',
        video: 'https://example.com/mv/2.mp4',
        avatar: 'https://picsum.photos/400/400?random=21',
        status: 'normal',
        playNum: 8000,
        commentNum: 400,
        likeNum: 1800,
        userId: 1,
        createTime: new Date().toISOString()
      }
    ]
  }

  // 获取分类列表
  if (url === '/content/category/list') {
    return [
      {
        id: 1,
        path: '1',
        content: '流行',
        children: []
      },
      {
        id: 2,
        path: '2',
        content: '摇滚',
        children: []
      },
      {
        id: 3,
        path: '3',
        content: '电子',
        children: []
      },
      {
        id: 4,
        path: '4',
        content: '古典',
        children: []
      }
    ]
  }

  // 获取歌曲分页
  if (url.startsWith('/content/song/page/')) {
    return {
      num: 1,
      size: 10,
      total: 100,
      item: [
        {
          id: 1,
          singerId: 1,
          userId: 1,
          music: 'https://example.com/music/1.mp3',
          name: '晴天',
          avatar: 'https://picsum.photos/200/200?random=30',
          album: '叶惠美',
          duration: '00:04:29',
          likeNum: 5000,
          playNum: 10000,
          commentNum: 1000,
          introduction: '经典情歌',
          status: 'normal',
          createTime: new Date().toISOString()
        },
        {
          id: 2,
          singerId: 1,
          userId: 1,
          music: 'https://example.com/music/2.mp3',
          name: '七里香',
          avatar: 'https://picsum.photos/200/200?random=31',
          album: '七里香',
          duration: '00:05:03',
          likeNum: 4500,
          playNum: 9000,
          commentNum: 900,
          introduction: '夏日恋曲',
          status: 'normal',
          createTime: new Date().toISOString()
        }
      ]
    }
  }

  // 获取专辑分页
  if (url.startsWith('/content/album/page/')) {
    return {
      num: 1,
      size: 10,
      total: 50,
      item: [
        {
          id: 1,
          singerId: 1,
          userId: 1,
          name: '叶惠美',
          avatar: 'https://picsum.photos/400/400?random=40',
          status: 'normal',
          playNum: 50000,
          likeNum: 10000,
          commentNum: 2000,
          introduction: '经典专辑',
          createTime: new Date().toISOString()
        }
      ]
    }
  }

  // 搜索相关
  if (url.startsWith('/search/search/')) {
    return {
      code: 0,
      msg: 'success',
      data: {
        num: 1,
        size: 10,
        total: 100,
        item: [
          {
            id: 1,
            name: '晴天',
            artist: '周杰伦',
            avatar: 'https://picsum.photos/200/200?random=50',
            type: 'song'
          }
        ]
      }
    }
  }

  // 验证码相关
  if (url === '/sms/captcha/create') {
    return {
      code: 0,
      msg: 'success',
      data: {
        key: 'captcha-key-123',
        img: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg=='
      }
    }
  }

  return { code: 200, data: [] }
}

// 认证相关 API
export const authAPI = {
  login: (params) => request('/auth/login', {
    method: 'POST',
    body: JSON.stringify(params)
  }),
  jwks: () => request('/auth/oauth2/jwks'),
  getUserList: (ids) => request('/auth/user/list', {
    params: { ids }
  })
}

// 内容相关 API
export const contentAPI = {
  // 歌曲管理
  getSongPage: (id, pageParam) => request(`/content/song/page/${id}`, {
    params: pageParam
  }),
  updateSong: (data) => request('/content/song/update', {
    method: 'PUT',
    body: JSON.stringify(data)
  }),

  // 歌单管理
  getSongListDetail: (songListId) => request(`/content/song-list/detail/${songListId}`),
  addSongList: (data) => request('/content/song-list/add', {
    method: 'POST',
    body: JSON.stringify(data)
  }),
  updateSongList: (data) => request('/content/song-list/update', {
    method: 'PUT',
    body: JSON.stringify(data)
  }),
  deleteSongList: (songListId) => request(`/content/song-list/delete/${songListId}`, {
    method: 'DELETE'
  }),

  // 歌手管理
  getSingerDetail: (id) => request(`/content/singer/${id}`),
  getTopSingers: () => request('/content/singer/top10'),
  addSinger: (data) => request('/content/singer/add', {
    method: 'POST',
    body: JSON.stringify(data)
  }),
  updateSinger: (params) => request('/content/singer/update', {
    method: 'PUT',
    params
  }),
  deleteSinger: (id) => request(`/content/singer/${id}`, {
    method: 'DELETE'
  }),

  // MV 管理
  getMvList: () => request('/content/mv/list'),
  getMvDetail: (mvId) => request(`/content/mv/detail/${mvId}`),
  getMvPage: (id, pageParam) => request(`/content/mv/page/${id}`, {
    params: pageParam
  }),
  addMv: (data) => request('/content/mv/add', {
    method: 'POST',
    body: JSON.stringify(data)
  }),
  updateMv: (data) => request('/content/mv/update', {
    method: 'PUT',
    body: JSON.stringify(data)
  }),
  deleteMv: (mvId) => request(`/content/mv/delete/${mvId}`, {
    method: 'DELETE'
  }),

  // 专辑管理
  getAlbumDetail: (albumId) => request(`/content/album/detail/${albumId}`),
  getAlbumPage: (id, pageParam) => request(`/content/album/page/${id}`, {
    params: pageParam
  }),
  addAlbum: (data) => request('/content/album/add', {
    method: 'POST',
    body: JSON.stringify(data)
  }),
  updateAlbum: (data) => request('/content/album/update', {
    method: 'PUT',
    body: JSON.stringify(data)
  }),
  deleteAlbum: (albumId) => request(`/content/album/delete/${albumId}`, {
    method: 'DELETE'
  }),

  // 分类管理
  getCategoryList: () => request('/content/category/list'),
  addCategory: (id, content) => request(`/content/category/add?id=${id}&content=${content}`, {
    method: 'POST'
  }),
  updateCategory: (id, content) => request(`/content/category/update?id=${id}&content=${content}`, {
    method: 'PUT'
  }),
  deleteCategory: (id) => request(`/content/category/delete?id=${id}`, {
    method: 'DELETE'
  }),

  // 歌单 - 歌曲关联管理
  getSongListSongIds: (songListId) => request(`/content/song-song-list/song-ids/${songListId}`),
  addSongToSongList: (songId, songListId) => request(`/content/song-song-list/add?songId=${songId}&songListId=${songListId}`, {
    method: 'POST'
  }),
  batchAddSongsToSongList: (songIds, songListId) => request(`/content/song-song-list/batch-add?songIds=${songIds.join(',')}&songListId=${songListId}`, {
    method: 'POST'
  }),
  removeSongFromSongList: (songId, songListId) => request(`/content/song-song-list/remove?songId=${songId}&songListId=${songListId}`, {
    method: 'DELETE'
  }),
  batchRemoveSongsFromSongList: (songIds, songListId) => request(`/content/song-song-list/batch-remove?songIds=${songIds.join(',')}&songListId=${songListId}`, {
    method: 'DELETE'
  })
}

// 媒体相关 API
export const mediaAPI = {
  merge: (total, fileMd5, fileName) => request(`/media/merge?total=${total}&fileMd5=${fileMd5}&fileName=${fileName}`),
  checkFile: (fileMd5, fileName) => request(`/media/check/file?fileMd5=${fileMd5}&fileName=${fileName}`),
  checkChunk: (id, chunkMd5, fileMd5) => request(`/media/check/chunk?id=${id}&chunkMd5=${chunkMd5}&fileMd5=${fileMd5}`),
  deleteUserMedia: (id) => request(`/media/user-media/del/${id}`, {
    method: 'DELETE'
  }),
  getUserMediaPage: (pageParam) => request('/media/user-media/page', {
    params: pageParam
  })
}

// 搜索相关 API
export const searchAPI = {
  findByType: (type, keyword, order, pageParam) => request(`/search/search/${type}`, {
    params: { keyword, order, ...pageParam }
  }),
  findAll: (keyword) => request('/search/search/all', {
    params: { keyword }
  })
}

// 短信服务 API
export const smsAPI = {
  validateCaptcha: (uuid, code) => request(`/sms/captcha/validate?uuid=${uuid}&code=${code}`),
  createCaptcha: () => request('/sms/captcha/create')
}

// 模拟的 API（用于前端开发）
export const mockAPI = {
  getSongs: (pageParam = { num: 1, size: 10 }) => request('/content/song/page/1', {
    params: pageParam
  }),
  getPlaylists: () => request('/content/song-list/detail/1'),
  search: (keyword) => request('/search/search/all', {
    params: { keyword }
  }),
  getRecommendations: () => request('/content/song-list/detail/1'),
  getFavoriteSongs: () => request('/content/song/page/1', {
    params: { num: 1, size: 10 }
  }),
  getTopSingers: () => request('/content/singer/top10'),
  getMvList: () => request('/content/mv/list'),
  getCategories: () => request('/content/category/list'),
  addToFavorites: (songId) => request(`/content/song-song-list/add?songId=${songId}&songListId=1`, {
    method: 'POST'
  }),
  removeFromFavorites: (songId) => request(`/content/song-song-list/remove?songId=${songId}&songListId=1`, {
    method: 'DELETE'
  }),
  login: (params) => request('/auth/login', {
    method: 'POST',
    body: JSON.stringify(params)
  }),
  getUserInfo: () => request('/auth/user/list', {
    params: { ids: [1] }
  })
}
