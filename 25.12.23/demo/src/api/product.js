import request from '@/utils/request'

export function getGoodsDetail (goodsId) {
  return request.get('/goods/detail', {
    params: {
      goodsId
    }
  })
}

export function getComments (goodsId, limit) {
  return request.get('/comment/listRows', {
    params: {
      goodsId,
      limit
    }
  })
}
