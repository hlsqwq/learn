/**
 * 目标1：默认显示-北京市天气
 *  1.1 获取北京市天气数据
 *  1.2 数据展示到页面
 */




function get_weather(code) {
  myAxios({
    url: 'http://hmajax.itheima.net/api/weather',
    method: 'get',
    params: {
      city: code
    }
  }).then(res => {
    console.log(res);
    render(res)

  }).catch(err => {
    console.log(err);

  })
}
get_weather('110100')

function render({ data }) {
  document.querySelector('.title').innerHTML = `<span class="dateShort">${data.dateShort}</span>
        <span class="calendar">农历&nbsp;
          <span class="dateLunar">${data.dateLunar}</span>
        </span>`
  document.querySelector("body > div > div.top-box > div.search-box > div.location > span").innerHTML = data.area
  document.querySelector("body > div > div.weather-box").innerHTML = ` <div class="tem-box">
        <span class="temp">
          <span class="temperature">${data.temperature}</span>
          <span>°</span>
        </span>
      </div>
      <div class="climate-box">
        <div class="air">
          <span class="psPm25">${data.psPm25}</span>
          <span class="psPm25Level">${data.psPm25Level}</span>
        </div>
        <ul class="weather-list">
          <li>
            <img src=${data.weatherImg} class="weatherImg" alt="">
            <span class="weather">${data.weather}</span>
          </li>
          <li class="windDirection">${data.windDirection}</li>
          <li class="windPower">${data.windPower}</li>
        </ul>
      </div>`
  const { todayWeather } = data
  document.querySelector("body > div > div.today-weather").innerHTML = `<div class="range-box">
        <span>今天：</span>
        <span class="range">
          <span class="weather">${todayWeather.weather}</span>
          <span class="temNight">${todayWeather.temNight}</span>
          <span>-</span>
          <span class="temDay">${todayWeather.temDay}</span>
          <span>℃</span>
        </span>
      </div>
      <ul class="sun-list">
        <li>
          <span>紫外线</span>
          <span class="ultraviolet">${todayWeather.ultraviolet}</span>
        </li>
        <li>
          <span>湿度</span>
          <span class="humidity">${todayWeather.humidity}</span>%
        </li>
        <li>
          <span>日出</span>
          <span class="sunriseTime">${todayWeather.sunriseTime}</span>
        </li>
        <li>
          <span>日落</span>
          <span class="sunsetTime">${todayWeather.sunsetTime}</span>
        </li>
      </ul>`

  let str = ''
  data.dayForecast.map(element => {
    str +=
      `<li class="item">
          <div class="date-box">
            <span class="dateFormat">${element.dateFormat}</span>
            <span class="date">${element.date}</span>
          </div>
          <img src=${element.weatherImg} alt="" class="weatherImg">
          <span class="weather">${element.weather}</span>
          <div class="temp">
            <span class="temNight">${element.temNight}</span>-
            <span class="temDay">${element.temDay}</span>
            <span>℃</span>
          </div>
          <div class="wind">
            <span class="windDirection">${element.windDirection}</span>
            <span class="windPower">&lt;${element.windPower}</span>
          </div>
        </li>`
  })
  document.querySelector("body > div > div.week-weather-box > ul").innerHTML = str
}



const input = document.querySelector("body > div > div.top-box > div.search-box > div.search > input")
input.addEventListener('input', shake(search, 500))

function shake(f, t) {
  let timer = null
  return function () {
    if (timer) {
      clearTimeout(timer)
    }
    setTimeout(function () {
      f()
      timer = null
    }, t)
  }
}


function search() {
  if (!input.value.length) {
    return
  }
  myAxios({
    url: 'http://hmajax.itheima.net/api/weather/city',
    method: 'get',
    params: {
      city: input.value
    }
  }).then(res => {
    if (res.data.length === 0) {
      return
    }
    let str = ''
    res.data.map(e => {
      str += `<li class="city-item" data-id=${e.code}>${e.name}</li>`
    })
    document.querySelector("body > div > div.top-box > div.search-box > div.search > ul").innerHTML = str
    Array.from(document.querySelector("body > div > div.top-box > div.search-box > div.search > ul").children).map(e => {
      e.addEventListener('click', function (ev) {
        get_weather(ev.target.dataset.id)
      })
    })
  })
}