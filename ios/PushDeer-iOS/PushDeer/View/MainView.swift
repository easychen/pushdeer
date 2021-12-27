//
//  MainView.swift
//  PushDeer
//
//  Created by HEXT on 2021/12/25.
//

import SwiftUI

/// APP 主界面
struct MainView: View {
  var body: some View {
    TabView {
      DeviceListView()
        .tabItem {
          Label("设备",systemImage: "ipad.and.iphone")
        }
      
      KeyListView()
        .tabItem{
          Label("Key",systemImage: "key")
        }
      
      MessageListView()
        .tabItem({Label("消息",systemImage: "message")}).onTapGesture {
        }
      
      SettingsView()
        .tabItem{
          Label("设置",systemImage: "gearshape")
        }
    }
  }
}

struct MainView_Previews: PreviewProvider {
  static var previews: some View {
    MainView()
  }
}
