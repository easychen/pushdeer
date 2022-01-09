//
//  SettingsView.swift
//  PushDeer
//
//  Created by HEXT on 2021/12/25.
//

import SwiftUI
//import StoreKit

/// 设置界面
struct SettingsView: View {
  @EnvironmentObject private var store: AppState
  
  var body: some View {
    BaseNavigationView(title: "设置") {
      VStack {
        SettingsItemView(title: "登录为 \(store.userInfo?.name ?? "--")", button: "退出") {
          store.token = ""
        }
        .padding(EdgeInsets(top: 18, leading: 20, bottom: 0, trailing: 20))
        
        SettingsItemView(title: "自定义服务器", button: "扫码") {
        }
        .disabled(true)
        .padding(EdgeInsets(top: 18, leading: 20, bottom: 0, trailing: 20))
        
        SettingsItemView(title: "喜欢PushDeer?", button: "评分") {
          let urlStr = "itms-apps://itunes.apple.com/app/id\(1596771139)?action=write-review"
          UIApplication.shared.open(URL(string: urlStr)!, options: [:], completionHandler: nil)
          // 直接弹出系统评分控件, 不过一年最多3次, 用户还可以在系统设置里面关
//          SKStoreReviewController.requestReview()
        }
        .padding(EdgeInsets(top: 18, leading: 20, bottom: 0, trailing: 20))
        
        Spacer()
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
}

struct SettingsView_Previews: PreviewProvider {
  static var previews: some View {
    SettingsView().environmentObject(AppState.shared)
  }
}
