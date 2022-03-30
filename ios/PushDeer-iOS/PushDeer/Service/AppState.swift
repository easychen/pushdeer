//
//  AppState.swift
//  PushDeer
//
//  Created by HEXT on 2022/1/4.
//

import Foundation
import AuthenticationServices

class AppState: ObservableObject {
  /// 账号 token
  @Published var token : String {
    didSet {
      UserDefaults.standard.set(token, forKey: "PushDeer_token")
    }
  }
  /// 设备列表
  @Published var devices: [DeviceItem] = []
  /// key 列表
  @Published var keys: [KeyItem] = []
  /// 消息列表
  //  @Published var messages: [MessageItem] = []
  /// 选中的 tab 下标
  @Published var tabSelectedIndex: Int {
    didSet {
      UserDefaults.standard.set(tabSelectedIndex, forKey: "PushDeer_tabSelectedIndex")
    }
  }
  /// 设备推送 token
  @Published var deviceToken: String = ""
  /// 用户信息
  @Published var userInfo: UserInfoContent?
  /// 是否显示测试发推送的 UI
  @Published var isShowTestPush: Bool {
    didSet {
      UserDefaults.standard.set(isShowTestPush, forKey: "PushDeer_isShowTestPush")
    }
  }
  /// 是否使用内置浏览器打开链接
  @Published var isUseBuiltInBrowser: Bool {
    didSet {
      UserDefaults.standard.set(isUseBuiltInBrowser, forKey: "PushDeer_isUseBuiltInBrowser")
    }
  }
  /// MarkDown BaseURL
  @Published var markDownBaseURL: String? {
    didSet {
      UserDefaults.standard.set(markDownBaseURL, forKey: "PushDeer_markDownBaseURL")
    }
  }
  
  /// API endpoint
  @Published var api_endpoint : String {
    didSet {
      UserDefaults.standard.set(api_endpoint, forKey: "PushDeer_api_endpoint")
    }
  }
  
  var isAppClip: Bool {
#if APPCLIP
    return true
#else
    return false
#endif
  }
  
  
  static let shared = AppState()
  private init() {
    let _token = UserDefaults.standard.string(forKey: "PushDeer_token")
    let _tabSelectedIndex = UserDefaults.standard.integer(forKey: "PushDeer_tabSelectedIndex")
    let _isShowTestPush = UserDefaults.standard.object(forKey: "PushDeer_isShowTestPush")
    let _isUseBuiltInBrowser = UserDefaults.standard.object(forKey: "PushDeer_isUseBuiltInBrowser")
    let _markDownBaseURL = UserDefaults.standard.string(forKey: "PushDeer_markDownBaseURL")
    let _api_endpoint = UserDefaults.standard.string(forKey: "PushDeer_api_endpoint")
    token = _token ?? ""
    tabSelectedIndex = _tabSelectedIndex
    isShowTestPush = _isShowTestPush as? Bool ?? true
    isUseBuiltInBrowser = _isUseBuiltInBrowser as? Bool ?? true
    markDownBaseURL = _markDownBaseURL
    api_endpoint = _api_endpoint ?? ""
  }
  
  func appleIdLogin(_ result: Result<ASAuthorization, Error>) async throws -> TokenContent {
    switch result {
    case let .success(authorization):
      if let appleIDCredential = authorization.credential as? ASAuthorizationAppleIDCredential {
        // 用户唯一ID，在一个开发者账号下的APP获取到的是一样的
        print(appleIDCredential.user) // 000791.7a323f1326dd4674bc16d32fd6339875.1424
        // 注意：当第一次认证成功之后，将不会再返回email，fullName等信息，可以在设置->Apple ID->密码与安全性->使用您AppleID的App 中删除对应的APP。
        print(appleIDCredential.email as Any) // easychen@qq.com
        print(appleIDCredential.fullName as Any) // givenName: lijie familyName: chen
        // 「JWT」格式的token，用于验证信息合法性。其值用.分割成3段, 中间一段base64后会看到包含了 用户唯一标识 和 邮箱 等字段
        let idToken = String(data:appleIDCredential.identityToken!, encoding: .utf8)
        print(idToken as Any)
        
        do {
          // 请求接口
          let result = try await HttpRequest.login(idToken: idToken!)
          print(result)
          // 登录成功
          return result
          
        } catch {
          print(error)
          // 后端登录失败
          throw NSError(domain: NSLocalizedString("登录失败", comment: "AppleId登录失败时提示") + "\n\(error.localizedDescription)", code: -4, userInfo: [NSLocalizedDescriptionKey: NSLocalizedString("登录失败", comment: "AppleId登录失败时提示") + "(-4)\n\(error.localizedDescription)"])
        }
      } else {
        // 非 Apple 登录凭证
        throw NSError(domain: NSLocalizedString("登录失败", comment: "AppleId登录失败时提示"), code: -3, userInfo: [NSLocalizedDescriptionKey: NSLocalizedString("登录失败", comment: "AppleId登录失败时提示") + "(-3)"])
      }
    case let .failure(error):
      print(error)
      if (error as NSError).code == 1001 {
        // Apple 登录取消
        throw NSError(domain: NSLocalizedString("登录失败", comment: "AppleId登录失败时提示") + "\n\(error.localizedDescription)", code: 1001, userInfo: [NSLocalizedDescriptionKey: NSLocalizedString("你已取消授权", comment: "")])
      }
      // Apple 登录失败
      throw NSError(domain: NSLocalizedString("登录失败", comment: "AppleId登录失败时提示") + "\n\(error.localizedDescription)", code: -2, userInfo: [NSLocalizedDescriptionKey: NSLocalizedString("登录失败", comment: "AppleId登录失败时提示") + "(-2)\n\(error.localizedDescription)"])
    }
  }
  
}
