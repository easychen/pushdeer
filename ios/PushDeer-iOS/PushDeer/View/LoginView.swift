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
      SignInWithAppleButton(
        onRequest: { request in
          
        },
        onCompletion: { result in
          
        }
      )
        .frame(height: 64)
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
