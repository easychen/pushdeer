//
//  PushDeerApp.swift
//  PushDeer
//
//  Created by HEXT on 2021/12/25.
//

import SwiftUI

@main
struct PushDeerApp: App {
  @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
  let store = AppState.shared
  let persistenceController = PersistenceController.shared
  
  var body: some Scene {
    WindowGroup {
      ContentView()
        .onReceive(NotificationCenter.default.publisher(for: UIApplication.willEnterForegroundNotification)) { _ in
          // 后台进入前台后, 清空未读消息角标
          UIApplication.shared.applicationIconBadgeNumber = 0
          // 后台进入前台后, 刷新本地消息列表
          if !AppState.shared.token.isEmpty {
            Task {
              let messageItems = try await HttpRequest.getMessages().messages
              try MessageModel.saveAndUpdate(messageItems: messageItems)
            }
          }
        }
        .environmentObject(store)
        .environment(\.managedObjectContext, persistenceController.container.viewContext)
    }
  }
}
