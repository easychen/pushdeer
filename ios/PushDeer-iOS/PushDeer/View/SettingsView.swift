//
//  SettingsView.swift
//  PushDeer
//
//  Created by HEXT on 2021/12/25.
//

import SwiftUI
import AuthenticationServices
//import StoreKit

/// 设置界面
struct SettingsView: View {
  @EnvironmentObject private var store: AppState
  
  var body: some View {
    BaseNavigationView(title: "设置") {
      ScrollView {
        VStack {
          SettingsItemView(title: NSLocalizedString("登录为", comment: "") + " " + userName(), button: NSLocalizedString("退出", comment: "退出登录按钮上的文字")) {
            store.token = ""
          }
          .padding(EdgeInsets(top: 18, leading: 20, bottom: 0, trailing: 20))
          
#if !targetEnvironment(macCatalyst) && !APPCLIP && !SELFHOSTED
          if WXApi.isWXAppInstalled() {
            LoginInfoView()
              .zIndex(-1)
              .padding(EdgeInsets(top: -30, leading: 20, bottom: 0, trailing: 20))
          }
#endif
          
          if Env.isSelfHosted {
            SettingsItemView(title: NSLocalizedString("API endpoint", comment: ""), button: NSLocalizedString("重置", comment: "")) {
              store.api_endpoint = ""
              store.token = ""
            }
            .padding(EdgeInsets(top: 18, leading: 20, bottom: 0, trailing: 20))
          }
          
          SettingsItemView(title: NSLocalizedString("喜欢PushDeer?", comment: ""), button: NSLocalizedString("评分", comment: "")) {
            let urlStr = "itms-apps://itunes.apple.com/app/id\(Env.appStoreId)?action=write-review"
            UIApplication.shared.open(URL(string: urlStr)!, options: [:], completionHandler: nil)
            // 直接弹出系统评分控件, 不过一年最多3次, 用户还可以在系统设置里面关
            //          SKStoreReviewController.requestReview()
          }
          .padding(EdgeInsets(top: 18, leading: 20, bottom: 0, trailing: 20))
          
#if !targetEnvironment(macCatalyst)
          CardView {
            HStack{
              Toggle(isOn: $store.isUseBuiltInBrowser) {
                Text("使用内置浏览器打开链接")
                  .font(.system(size: 18))
                  .foregroundColor(Color("textColor"))
              }
              .padding(16)
              .toggleStyle(SwitchToggleStyle(tint: .accentColor))
            }
            .frame(height: 74)
          }
          .padding(EdgeInsets(top: 18, leading: 20, bottom: 0, trailing: 20))
#endif
          
          Spacer()
        }
      }
    }
    .onAppear {
      if store.userInfo != nil {
        return
      }
      Task {
        store.userInfo = try await HttpRequest.getUserInfo()
      }
    }
  }
  
  func userName() -> String {
    if let name = store.userInfo?.name {
      if name.isEmpty {
        return NSLocalizedString("苹果用户", comment: "")
      } else {
        return name
      }
    } else {
      return "--"
    }
  }
}

struct LoginInfoView: View {
  enum AlertType : Identifiable {
    var id: Self { self }
    case apple
    case wechat
  }
  @EnvironmentObject private var store: AppState
  @State private var alertType: AlertType? = nil
  static private var coordinator: AppleSignInCoordinator? = nil
  var body: some View {
    CardView {
      HStack(spacing: 16) {
        Spacer()
        Circle()
          .frame(width: 10, height: 10, alignment: .center)
          .foregroundColor(store.userInfo?.apple_id?.isEmpty ?? true ? .gray : .green)
        Button {
          if store.userInfo?.apple_id?.isEmpty ?? true {
            alertType = .apple
          } else {
            HToast.showText(NSLocalizedString("当前已经绑定苹果账号", comment: ""))
          }
        } label: {
          Image(systemName: "applelogo")
            .resizable()
            .scaledToFit()
            .frame( height: 40, alignment: .center)
        }
        Spacer()
        Circle()
          .frame(width: 10, height: 10, alignment: .center)
          .foregroundColor(store.userInfo?.wechat_id?.isEmpty ?? true ? .gray : .green)
        Button {
          if store.userInfo?.wechat_id?.isEmpty ?? true {
            alertType = .wechat
          } else {
            HToast.showText(NSLocalizedString("当前已经绑定微信账号", comment: ""))
          }
        } label: {
          Image("weixin-login")
            .resizable()
            .renderingMode(.template)
            .scaledToFit()
            .frame(height: 37, alignment: .center)
        }
        Spacer()
      }
      .padding(EdgeInsets(top: 32, leading: 0, bottom: 12, trailing: 0))
    }
    .alert(item: $alertType) { alertType in
      var message = ""
      switch alertType {
      case .apple:
        message = NSLocalizedString("准备绑定苹果账号, 如果你绑定的账号之前已经存在, 则会合并到当前账号. (之前的Key可能会被删除)", comment: "")
      case .wechat:
        message = NSLocalizedString("准备绑定微信账号, 如果你绑定的账号之前已经存在, 则会合并到当前账号. (之前的Key可能会被删除)", comment: "")
      }
      
      return Alert(
        title: Text("温馨提示"),
        message: Text(message),
        primaryButton: .default(
          Text("绑定"),
          action: {
            switch alertType {
            case .apple:
              LoginInfoView.coordinator = AppleSignInCoordinator(
                onRequest: { request in
                  request.requestedScopes = [.fullName, .email]
                },
                onCompletion: { result in
                  do {
                    switch result {
                    case let .success(authorization):
                      if let appleIDCredential = authorization.credential as? ASAuthorizationAppleIDCredential {
                        let idToken = String(data:appleIDCredential.identityToken!, encoding: .utf8)
                        print(idToken as Any)
                        // 请求接口
                        let result = try await HttpRequest.mergeUser(type: "apple", tokenorcode: idToken!)
                        print(result)
                        // 合并成功, 更新数据
                        store.userInfo = try await HttpRequest.getUserInfo()
                      }
                    case let .failure(error):
                      if (error as NSError).code == 1001 {
                        HToast.showWarning(NSLocalizedString("你已取消授权", comment: ""))
                      } else {
                        HToast.showError(error.localizedDescription)
                      }
                    }
                  } catch {
                    HToast.showError(error.localizedDescription)
                  }
                }
              )
              LoginInfoView.coordinator?.performRequests()
              
            case .wechat:
#if !targetEnvironment(macCatalyst) && !APPCLIP && !SELFHOSTED
              let req = SendAuthReq()
              req.scope = "snsapi_userinfo";
              req.state = "bind";
              WXApi.send(req) { b in
                print("WXApi.send:", b)
              }
              // 微信登录请求发出去后面的逻辑在 AppDelegate 的 onResp 回调方法中处理
#endif
            }
          }
        ),
        secondaryButton: .cancel(Text("稍后"))
      )
    }
  }
}

struct SettingsView_Previews: PreviewProvider {
  static var previews: some View {
    SettingsView().environmentObject(AppState.shared)
  }
}
