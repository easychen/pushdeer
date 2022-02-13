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
      ScrollView {
        VStack {
          SettingsItemView(title: NSLocalizedString("登录为", comment: "") + " " + userName(), button: NSLocalizedString("退出", comment: "退出登录按钮上的文字")) {
            store.token = ""
          }
          .padding(EdgeInsets(top: 18, leading: 20, bottom: 0, trailing: 20))
          
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

struct SettingsView_Previews: PreviewProvider {
  static var previews: some View {
    SettingsView().environmentObject(AppState.shared)
  }
}
