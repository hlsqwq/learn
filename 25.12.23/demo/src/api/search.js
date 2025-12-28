import request from '@/utils/request'

export function searchGoods (categoryId, goodsName) {
  return request.get('/goods/list', {
    params: {
      categoryId,
      goodsName,
      page: 1
    }
  })
}
