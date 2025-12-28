
const USER_INFO_KEY = 'USER_INFO_KEY'
const HISTORY_KEY = 'HISTORY_KEY'

export function getHistory () {
  const list = localStorage.getItem(HISTORY_KEY)
  return list ? JSON.parse(list) : []
}
export function setHistory (list) {
  localStorage.setItem(HISTORY_KEY, JSON.stringify(list))
}

export function getUserInfo () {
  const obj = localStorage.getItem(USER_INFO_KEY)
  return obj ? JSON.parse(obj) : {}
}
export function setUserInfo (info) {
  localStorage.setItem(USER_INFO_KEY, JSON.stringify(info))
}
