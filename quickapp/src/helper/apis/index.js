/**
 * 导出 apis 下目录的所有接口
 */
const files = require.context('.', true, /\.js/)
const modules = {}

files.keys().forEach(key => {
  if (key === './index.js') {
    return
  }
  modules[key.replace(/(^\.\/|\.js$)/g, '')] = files(key).default
})

export default modules
