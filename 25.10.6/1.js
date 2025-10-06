


function my_axios(config) {
  return new Promise((resolve, reject) => {
    const xml = new XMLHttpRequest()
    if (config.params) {
      const s = new URLSearchParams(config.params)
      const str = s.toString()
      config.url += `?${str}`
    }
    xml.open(config.method || 'get', config.url)
    xml.addEventListener('loadend', function () {
      if (xml.status >= 200 && xml.status < 300) {
        resolve(JSON.parse(xml.response))
      } else {
        reject(new Error(xml.response))
      }
    })
    if (config.data) {
      xml.setRequestHeader('Content-Type', 'application/json')
      xml.send(JSON.stringify(config.data))
    } else {
      xml.send()
    }
  })
}