

// console.log('hello world');

// const fs = require('fs')
// const path = require('path')
// console.log(path.join(__dirname, '1.txt'));



// fs.writeFile('1.txt', 'hello world', err => {
//   if (err) {
//     console.log(err);

//   } else {
//     console.log('success');

//   }
// })


// fs.readFile('1.txt', (err, data) => {
//   if (err) {
//     console.log(err);
//   }
//   else {
//     console.log(data.toString());
//   }
// })



// const http = require('http')
// const server = http.createServer()
// const fs = require('fs')
// const path = require('path')


// server.on('request', (req, res) => {
//   console.log(req.url);

//   if (req.url === '/1') {
//     fs.readFile(path.join(__dirname, '04/dist/index.html'), (err, data) => {
//       if (err) {
//         console.log(err);
//       } else {
//         res.setHeader('Content-Type', 'text/html;charset=utf-8')
//         res.end(data.toString())
//       }
//     })
//     return
//   }
//   res.setHeader('Content-Type', 'text/plain;charset=utf-8')
//   res.end('hello world')
// })

// server.listen(3000, () => {
//   console.log('server start');
// })



// const obj = require('./2.js')
// obj.func()

import { func } from './2.js'

func()

import dayjs from 'dayjs'
console.log(dayjs().format('YYYY-MM-DD'));
