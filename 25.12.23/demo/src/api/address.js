import request from '@/utils/request'

export function getAddress () {
  return request.get('/address/list')
}
