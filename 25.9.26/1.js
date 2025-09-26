



// let price = +prompt('goods price?')
// let num = +prompt('goods count?')
// let total = price * num
// document.querySelector('.price').innerHTML = price
// document.querySelector('.count').innerHTML = num
// document.querySelector('.total').innerHTML = total



let arr = []

for (let i = 0; i < 4; i++) {
  arr.push(+prompt(`enter the ${i + 1} season sales`))
}

let str = []

for (let i = 0; i < arr.length; i++) {
  console.log(arr.length);

  str.push(`
    <div class="box" style="height: ${arr[i] / 3}%;">
      <div class="tit">${arr[i]}</div>
      <div class="bot">${i + 1} season</div>
    </div>
  `)
}
document.querySelector('.wrapper').innerHTML = str.join(' ')


