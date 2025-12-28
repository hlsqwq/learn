import request from '@/utils/request'
export function addGoods (goodsId, goodsNum, goodsSkuId) {
  return request.post('/cart/add', {
    goodsId,
    goodsNum,
    goodsSkuId
  })
}

export function getTotal () {
  return request.get('/cart/total')
}

export function getCart () {
  return request.get('/cart/list')
}

export function changeCart (goodsId, goodsNum, goodsSkuId) {
  return request.post('/cart/update', {
    goodsId,
    goodsNum,
    goodsSkuId
  })
}

export function delGoods (cartIds) {
  request.post('/cart/clear', { cartIds })
}
