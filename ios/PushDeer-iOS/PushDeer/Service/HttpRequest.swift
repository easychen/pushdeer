//
//  HttpRequest.swift
//  PushDeer
//
//  Created by HEXT on 2022/1/4.
//

import Foundation
import Moya

struct TokenAuthorizationPlugin: PluginType {
  
  func prepare(_ request: URLRequest, target: TargetType) -> URLRequest {
    if
      let url = request.url,
      var components = URLComponents(url: url, resolvingAgainstBaseURL: true),
      let queryItems =  components.queryItems
    {
      let queryItems_new = queryItems.map { item -> URLQueryItem in
        if item.name == "token" {
          // 把请求参数中的 token 都换成最新的
          return URLQueryItem(name: "token", value: AppState.shared.token)
        }
        return item
      }
      components.queryItems = queryItems_new
      var request_mutable = request
      request_mutable.url = components.url
      return request_mutable
    }
    return request
  }
}

@MainActor
struct HttpRequest {
  
  static let provider = MoyaProvider<PushDeerApi>(callbackQueue: DispatchQueue.main, plugins: [TokenAuthorizationPlugin()])
  
  /// 统一处理接口请求, 并且封装成 Swift Concurrency 模式 (async / await)
  static func request<T: Codable>(_ targetType: PushDeerApi, resultType: T.Type) async throws -> T {
    do {
      return try await _request(targetType, resultType: resultType)
    } catch {
      if (error as NSError).code == 80403 {
        // token 失效处理
        let stoken = AppState.shared.getLocalSToken() // 取本地 stoken
        if isNotEmpty(stoken) {
          let token = try? await stokenLogin(stoken: stoken!).token // 用 stoken 登录换 token
          if isNotEmpty(token) {
            AppState.shared.token = token! // 更新 token
            do {
              return try await _request(targetType, resultType: resultType) // 用新 token 再次请求原接口
            } catch {
              throw error // 再次请求报错
            }
          }
          AppState.shared.deleteLocalSToken() // 到这来说明 stoken 已经失效, 删掉 stoken
        }
        // 退出登录
        AppState.shared.token = "" // 到这来 说明 stoken 不存在 或 已经失效, 清空 token 使其切换到登录界面
      }
      throw error // 抛出原请求的 error (stoken流程失败, 或者不是token失效的错误, 会到这儿来)
    }
  }
  
  static func _request<T: Codable>(_ targetType: PushDeerApi, resultType: T.Type) async throws -> T {
    return try await withCheckedThrowingContinuation { continuation in
      provider.request(targetType) { result in
        switch result {
        case let .success(response):
          do {
            print(response)
            if response.statusCode != 200 {
              continuation.resume(throwing: NSError(domain: NSLocalizedString("接口报错", comment: "接口报错时提示"), code: response.statusCode, userInfo: [NSLocalizedDescriptionKey: NSLocalizedString("接口报错", comment: "接口报错时提示") + "(\(response.statusCode)"]))
              return
            }
            print((try? response.mapJSON()) ?? "返回值解析失败")
            let result = try JSONDecoder().decode(ApiResult<T>.self, from: response.data)
            print(result)
            if let content = result.content, result.code == 0 {
              continuation.resume(returning: content)
            } else if result.code == 80403 {
              continuation.resume(throwing: NSError(domain: result.error ?? NSLocalizedString("登录过期", comment: "token失效时提示"), code: result.code, userInfo: [NSLocalizedDescriptionKey: result.error ?? NSLocalizedString("登录过期", comment: "token失效时提示")]))
            } else {
              continuation.resume(throwing: NSError(domain: result.error ?? NSLocalizedString("接口报错", comment: "接口报错时提示"), code: result.code, userInfo: [NSLocalizedDescriptionKey: result.error ?? NSLocalizedString("接口报错", comment: "接口报错时提示")]))
            }
          } catch {
            print(error)
            continuation.resume(throwing: error)
          }
        case let .failure(error):
          print(error)
          continuation.resume(throwing: error)
        }
      }
    }
  }
  
  static func fake() async throws -> TokenContent {
    return try await request(.fake, resultType: TokenContent.self)
  }
  
  static func login(idToken: String) async throws -> TokenContent {
    return try await request(.login(idToken: idToken), resultType: TokenContent.self)
  }
  
  static func wechatLogin(code: String) async throws -> TokenContent {
    return try await request(.wechatLogin(code: code), resultType: TokenContent.self)
  }
  
  /// 合并用户并将旧用户删除
  /// | 参数 | 说明 |
  /// | - | - |
  /// | type | 字符串，必须为 apple 或者 wechat |
  /// | tokenorcode | type 为 apple时此字段为 idToken，否则为 微信code |
  static func mergeUser(type: String, tokenorcode: String) async throws -> ResultContent {
    return try await request(.mergeUser(token: AppState.shared.token, type: type, tokenorcode: tokenorcode), resultType: ResultContent.self)
  }
  
  static func getUserInfo() async throws -> UserInfoContent {
    return try await request(.getUserInfo(token: AppState.shared.token), resultType: UserInfoContent.self)
  }
  
  static func regDevice() async throws -> DeviceContent {
    return try await request(.regDevice(
      token: AppState.shared.token,
      name: UIDevice.current.name,
      device_id: AppState.shared.deviceToken,
      is_clip: AppState.shared.isAppClip ? 1 : 0
    ), resultType: DeviceContent.self)
  }
  
  static func rmDevice(id: Int) async throws -> ActionContent {
    return try await request(.rmDevice(token: AppState.shared.token, id: id), resultType: ActionContent.self)
  }
  static func renameDevice(id: Int, name: String) async throws -> ActionContent {
    return try await request(.renameDevice(token: AppState.shared.token, id: id, name: name), resultType: ActionContent.self)
  }
  static func getDevices() async throws -> DeviceContent {
    return try await request(.getDevices(token: AppState.shared.token), resultType: DeviceContent.self)
  }
  static func loadDevices() {
    _Concurrency.Task {
      let result = try await getDevices()
      AppState.shared.devices = result.devices
    }
  }
  
  static func genKey() async throws -> KeyContent {
    return try await request(.genKey(token: AppState.shared.token), resultType: KeyContent.self)
  }
  
  static func regenKey(id: Int) async throws -> ActionContent {
    return try await request(.regenKey(token: AppState.shared.token, id: id), resultType: ActionContent.self)
  }
  
  static func renameKey(id: Int, name: String) async throws -> ActionContent {
    return try await request(.renameKey(token: AppState.shared.token, id: id, name: name), resultType: ActionContent.self)
  }
  
  static func rmKey(id: Int) async throws -> ActionContent {
    return try await request(.rmKey(token: AppState.shared.token, id: id), resultType: ActionContent.self)
  }
  
  static func getKeys() async throws -> KeyContent {
    return try await request(.getKeys(token: AppState.shared.token), resultType: KeyContent.self)
  }
  static func loadKeys() {
    _Concurrency.Task {
      let result = try await getKeys()
      AppState.shared.keys = result.keys
    }
  }
  
  static func push(pushkey: String, text: String, desp: String, type: String) async throws -> ResultContent {
    return try await request(.push(pushkey: pushkey, text: text, desp: desp, type: type), resultType: ResultContent.self)
  }
  
  static func getMessages() async throws -> MessageContent {
    return try await request(.getMessages(token: AppState.shared.token, limit: 100), resultType: MessageContent.self)
  }
  
  static func rmMessage(id: Int) async throws -> ActionContent {
    return try await request(.rmMessage(token: AppState.shared.token, id: id), resultType: ActionContent.self)
  }
  
  static func rmAllMessage() async throws -> ActionContent {
    return try await request(.rmAllMessage(token: AppState.shared.token), resultType: ActionContent.self)
  }
  
  
  static func stokenLogin(stoken: String) async throws -> TokenContent {
    return try await request(.stokenLogin(stoken: stoken), resultType: TokenContent.self)
  }
  
  static func stokenRegen() async throws -> STokenContent {
    return try await request(.stokenRegen(token: AppState.shared.token), resultType: STokenContent.self)
  }
  
  static func stokenRemove() async throws -> STokenContent {
    return try await request(.stokenRemove(token: AppState.shared.token), resultType: STokenContent.self)
  }
}
