import request from '@/utils/request'

export function getHome () {
  return request.get('/page/detail', {
    params: {
      pageId: 0
    },
    headers: {
      platform: 'h5'
    }
  })
}

export function getHomeCategory () {
  return request.get('/category/list')
}
