//
//  SettingsView.swift
//  PushDeer
//
//  Created by HEXT on 2021/12/25.
//

import SwiftUI

/// 设置界面
struct SettingsView: View {
  var body: some View {
    BaseNavigationView(title: "设置") {
      VStack {
        SettingsItemView(title: "登录为 Hext", button: "退出") {
        }
        .padding(EdgeInsets(top: 18, leading: 20, bottom: 0, trailing: 20))
        
        SettingsItemView(title: "自定义服务器", button: "扫码") {
        }
        .padding(EdgeInsets(top: 18, leading: 20, bottom: 0, trailing: 20))
        
        SettingsItemView(title: "喜欢PushDeer?", button: "评分") {
        }
        .padding(EdgeInsets(top: 18, leading: 20, bottom: 0, trailing: 20))
        
        Spacer()
      }
    }
  }
}

struct SettingsView_Previews: PreviewProvider {
  static var previews: some View {
    SettingsView()
  }
}
