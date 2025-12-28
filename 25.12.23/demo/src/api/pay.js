
import request from '@/utils/request'

export function getOrder (mode, obj) {
  return request.get('/checkout/order', {
    params: {
      mode,
      delivery: 10,
      shopId: 0,
      couponId: 0,
      isUsePoints: 0,
      ...obj
    }
  })
}

export function orderSubmit ({ goodsId, goodsNum, goodsSkuId, mode, remark, cartIds }) {
  return request.post('/checkout/submit', {
    goodsId,
    goodsNum,
    goodsSkuId,
    delivery: 10,
    couponId: 0,
    isUsePoints: 0,
    mode,
    payType: 10,
    remark,
    cartIds
  })
}
