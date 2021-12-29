import $ajax from '../ajax'

/**
 * @desc 在实际开发中，您可以将 baseUrl 替换为您的请求地址前缀；
 *
 * 已将 $apis 挂载在 global，您可以通过如下方式，进行调用：
 * $apis.example.getApi().then().catch().finally()
 *
 * 备注：如果您不需要发起请求，删除 apis 目录，以及 app.ux 中引用即可；
 */
const baseUrl = 'https://api.exampel.com/'

export default {
  getApi(data) {
    return $ajax.get(`${baseUrl}your-project-api`, data)
  },
  postOtherApi(data) {
    return $ajax.post(`${baseUrl}your-project-api`, data)
  }
}
