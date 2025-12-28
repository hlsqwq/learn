
import request from '@/utils/request'

export function getOrderList (dataType) {
  return request.get('/order/list', {
    params: {
      dataType,
      page: 1
    }
  })
}
