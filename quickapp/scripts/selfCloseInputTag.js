/**
 * @file: selfCloseInputTag.js
 * @desc: 遍历指定目录下 .ux 文件，将其中 input 标签由 <input **></input> 转换为 <input ** />
 * @date: 2019-01-23
 */

const fs = require('fs')
const path = require('path')

const quickappCodePath = './src/'

const main = codePath => {
  const traversing = cpath => {
    const files = fs.readdirSync(cpath)
    files.forEach(fileName => {
      const fPath = path.join(cpath, fileName)
      const stats = fs.statSync(fPath)
      stats.isDirectory() && traversing(fPath)
      stats.isFile() && fPath.endsWith('.ux') && matchAndReplace(fPath)
    })
  }
  traversing(codePath)
}

const matchAndReplace = path => {
  const pageContent = fs.readFileSync(path, 'UTF-8')
  const newContent = pageContent.replace(
    /(<)([\s]*?)(input\b[^\/]*?)>[\s\S]*?<\/input>/gm,
    '$1$3 />'
  )
  fs.writeFile(path, newContent, error => {
    if (error) throw `Something went wrong: ${error}`
  })
}

main(quickappCodePath)
