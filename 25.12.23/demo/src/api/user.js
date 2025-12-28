
import request from '@/utils/request'

export function getMy () {
  return request.get('/user/info')
}
