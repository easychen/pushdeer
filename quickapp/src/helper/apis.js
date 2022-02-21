import $ajax from './ajax'

/**
 * @desc 在实际开发中，您可以将 baseUrl 替换为您的请求地址前缀；
 *
 * 已将 $apis 挂载在 global，您可以通过如下方式，进行调用：
 * $apis.example.getApi().then().catch().finally()
 *
 * 备注：如果您不需要发起请求，删除 apis 目录，以及 app.ux 中引用即可；
 */
const baseUrl = 'YOUR_API_ADDRESS'

let token = ""

function to(promise) {
  return promise
    .catch(err => { $utils.showToast(err) });
}

export default {
  setToken(t) {
    token = t
  },
  login() {
    return to($ajax.get(`${baseUrl}login/fake`).then(res => res.token))
  },
  wxLogin(code) {
    return $ajax.post(`${baseUrl}login/wecode`, { code }).then(res => res.token)
  },
  userInfo() {
    return $ajax.post(`${baseUrl}user/info`, { token })
  },
  // device
  deviceReg(name, device_id) {
    return $ajax.post(`${baseUrl}device/reg`, { token, is_clip: 1, name, device_id })
      .then(res => res.devices)
  },
  deviceList() {
    return $ajax.post(`${baseUrl}device/list`, { token })
      .then(res => res.devices)
  },
  deviceRename(id, name) {
    return $ajax.post(`${baseUrl}device/rename`, { token, id, name })
  },
  deviceRemove(id) {
    return $ajax.post(`${baseUrl}device/remove`, { token, id })
  },
  // key
  keyGen() {
    return to($ajax.post(`${baseUrl}key/gen`, { token }))
      .then(res => res.keys)
  },
  keyList() {
    return to($ajax.post(`${baseUrl}key/list`, { token }))
      .then(res => res.keys)
  },
  keyRename(id, name) {
    return to($ajax.post(`${baseUrl}key/rename`, { token, id, name }))
  },
  keyRegen(id) {
    return to($ajax.post(`${baseUrl}key/regen`, { token, id }))
  },
  keyRemove(id) {
    return to($ajax.post(`${baseUrl}key/remove`, { token, id }))
  },
  // message
  messageList() {
    return to($ajax.post(`${baseUrl}message/list`, { token }))
      .then(res => res.messages)
    // [{"id":3,"uid":"1","text":"\u8fd9\u662f\u4ec0\u4e48\u54401111","desp":"","type":"markdown","created_at":"2021-12-22T12:09:46.000000Z"},{"id":2,"uid":"1","text":"\u8fd9\u662f\u4ec0\u4e48\u5440234","desp":"","type":"markdown","created_at":"2021-12-22T12:08:32.000000Z"}]
  },
  messagePush(text, desp, type) {
    return to($ajax.post(`${baseUrl}message/push`, { token, text, desp, type }))
  },
  messageRemove(id) {
    return to($ajax.post(`${baseUrl}message/remove`, { token, id }))
  }
}
