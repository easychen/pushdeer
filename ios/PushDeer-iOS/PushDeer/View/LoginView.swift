//
//  LoginView.swift
//  PushDeer
//
//  Created by HEXT on 2021/12/25.
//

import SwiftUI
import AuthenticationServices

/// 登录界面
struct LoginView: View {
  
  @EnvironmentObject private var store: AppState
  @State private var showLoading = false
  
  var body: some View {
    VStack{
      Spacer()
      Image("logo.with.space")
        .resizable()
        .scaledToFit()
      if Env.isSelfHosted {
        Button("重置API endpoint") {
          store.api_endpoint = ""
        }
      }
      Spacer()
      if showLoading {
        ProgressView()
          .scaleEffect(1.5)
          .frame(height: 64)
      } else {
        // 苹果登录按钮
        AppleSignInButton(
          onRequest: { request in
            request.requestedScopes = [.fullName, .email]
          },
          onCompletion: { result in
            do {
              showLoading = true
              store.token = try await store.appleIdLogin(result).token
              // 获取成功去主页
            } catch {
              showLoading = false
              if (error as NSError).code == 1001 {
                // 取消登录
                HToast.showWarning(error.localizedDescription)
                return
              }
              HToast.showError(error.localizedDescription)
            }
          }
        )
          .overlay(RoundedRectangle(cornerRadius: 6).stroke(Color.white))
          .frame(maxWidth: 375, minHeight: 64, maxHeight: 64)
          .padding()
        
#if !targetEnvironment(macCatalyst) && !APPCLIP && !SELFHOSTED
        if WXApi.isWXAppInstalled() {
          // 微信登录按钮
          Button {
            let req = SendAuthReq()
            req.scope = "snsapi_userinfo";
            req.state = "login";
            WXApi.send(req) { b in
              print("WXApi.send:", b)
            }
            // 微信登录请求发出去后面的逻辑在 AppDelegate 的 onResp 回调方法中处理
          } label: {
            HStack {
              Image("weixin-login")
                .resizable()
                .renderingMode(.template)
                .scaledToFit()
                .frame(height:20)
              Text("通过微信登录")
            }
            .font(.system(size: 26, weight: .semibold))
            .foregroundColor(Color("weixinFgColor"))
            .frame(maxWidth: 375, minHeight: 64, maxHeight: 64)
          }
          .background(Color("weixinBgColor"))
          .cornerRadius(6)
          .overlay(RoundedRectangle(cornerRadius: 6).stroke(Color("weixinFgColor")))
          .padding()
        }
#endif
      }
      Spacer()
    }
    .padding()
  }
}

struct LoginView_Previews: PreviewProvider {
  static var previews: some View {
    LoginView()
  }
}
