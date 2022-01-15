//
//  HttpRequest.swift
//  PushDeer
//
//  Created by HEXT on 2022/1/4.
//

import Foundation
import Moya

@MainActor
struct HttpRequest {
  
  static let provider = MoyaProvider<PushDeerApi>(callbackQueue: DispatchQueue.main)
  
  /// 统一处理接口请求, 并且封装成 Swift Concurrency 模式 (async / await)
  static func request<T: Codable>(_ targetType: PushDeerApi, resultType: T.Type) async throws -> T {
    return try await withCheckedThrowingContinuation { continuation in
      provider.request(targetType) { result in
        switch result {
        case let .success(response):
          do {
            let result = try JSONDecoder().decode(ApiResult<T>.self, from: response.data)
            print(result)
            if let content = result.content, result.code == 0 {
              continuation.resume(returning: content)
            } else if result.code == 80403 {
              AppState.shared.token = ""
              continuation.resume(throwing: NSError(domain: result.error ?? NSLocalizedString("登录过期", comment: "token失效时提示"), code: result.code, userInfo: nil))
            } else {
              continuation.resume(throwing: NSError(domain: result.error ?? NSLocalizedString("接口报错", comment: "接口报错时提示"), code: result.code, userInfo: nil))
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
  
  static func push(pushkey: String, text: String, desp: String, type: String) async throws -> PushResultContent {
    return try await request(.push(pushkey: pushkey, text: text, desp: desp, type: type), resultType: PushResultContent.self)
  }
  
  static func getMessages() async throws -> MessageContent {
    return try await request(.getMessages(token: AppState.shared.token, limit: 100), resultType: MessageContent.self)
  }
  
  static func rmMessage(id: Int) async throws -> ActionContent {
    return try await request(.rmMessage(token: AppState.shared.token, id: id), resultType: ActionContent.self)
  }
}
