//
//  WXDelegate.swift
//  PushDeer
//
//  Created by HEXT on 2022/2/26.
//

import Foundation

#if !targetEnvironment(macCatalyst) && !APPCLIP && !SELFHOSTED

@MainActor
class WXDelegate: NSObject, WXApiDelegate {
  
  static let shared = WXDelegate()
  private override init() { super.init() }
  
  func onReq(_ req: BaseReq) {
    print(#function, req.type, req.openID)
  }
  func onResp(_ resp: BaseResp) {
    print(#function, resp.type, resp.errCode, resp.errStr)
    if let resp = resp as? SendAuthResp { // 是登录授权的响应
      print(resp.code as Any, resp.state as Any, resp.lang as Any, resp.country as Any)
      switch resp.errCode {
      case 0: // 用户同意
        if let code = resp.code, let state = resp.state {
          Task {
            do {
              if state == "login" {
                AppState.shared.token = try await HttpRequest.wechatLogin(code: code).token
                // 给 AppState 的 token 赋值后, SwiftUI 写的 ContentView 页面会监听到并自动进入主页
                // 登录成功后的处理
                AppState.shared.loginAfter()
                
              } else if state == "bind" {
                _ = try await HttpRequest.mergeUser(type: "wechat", tokenorcode: code)
                // 合并成功, 更新数据
                AppState.shared.userInfo = try await HttpRequest.getUserInfo()
              }
            } catch {
              HToast.showError(error.localizedDescription)
            }
          }
        }
        break
      case -2: // 用户取消
        HToast.showWarning(NSLocalizedString("你已取消授权", comment: ""))
        break
      case -4: // 用户拒绝授权
        HToast.showError(NSLocalizedString("你已拒绝授权", comment: ""))
        break
      default:
        break
      }
    }
  }
}

#endif
