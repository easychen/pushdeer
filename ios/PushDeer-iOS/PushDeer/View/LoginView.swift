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
  var body: some View {
    VStack{
      Spacer()
      Image("logo.with.space")
        .resizable()
        .scaledToFit()
      Spacer()
      AppleSignInButton(
        onRequest: { request in
          request.requestedScopes = [.fullName, .email]
        },
        onCompletion: { result in
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
              
              Task {
                do {
                  let result = try await HttpRequest.login(idToken: idToken!)
                  
                  print(result)
                } catch {
                  print(error)
                }
              }
            }
          case let .failure(error):
            print(error)
          }
        }
      )
        .frame(maxWidth: 375, minHeight: 64, maxHeight: 64)
        .padding()
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
