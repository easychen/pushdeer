//
//  AppDelegate.swift
//  PushDeer
//
//  Created by HeXiaoTian on 2021/12/31.
//

import UIKit
import UserNotifications
import IQKeyboardManagerSwift

@MainActor
class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate {
  
  func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
    
    // 注册通知
    let center = UNUserNotificationCenter.current()
    center.delegate = self
    center.requestAuthorization(options: [.badge, .sound, .alert]) { granted, error in
      print("注册通知结果: \(granted) - \(String(describing: error))")
    }
    application.registerForRemoteNotifications()
    
    // 首次网络请求提示
    Task {
      let notFirstStart = UserDefaults.standard.bool(forKey: "PushDeer_notFirstStart")
      if !notFirstStart {
        // APP首次启动后要先调一个无用接口, 用于触发国行手机的网络授权弹框, 未授权前调的接口会直接失败. (提前触发网络授权弹窗)
        _ = try await HttpRequest.fake()
        UserDefaults.standard.set(true, forKey: "PushDeer_notFirstStart")
      }
    }
    
    // IQ键盘管理
    IQKeyboardManager.shared.enable = false // 键盘与输入框的距离管理 禁用
    IQKeyboardManager.shared.enableAutoToolbar = true // 键盘上方添加的工具栏 启用
    
#if !targetEnvironment(macCatalyst) && !APPCLIP && !SELFHOSTED
    // 非Mac and 非AppClip and 非自架版
    // 向微信注册
    WXApi.registerApp(Env.wxAppid, universalLink: Env.wxUniversalLink)
#endif

    return true
  }
  
  func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
    let deviceTokenString = deviceToken.reduce("", {$0 + String(format: "%02X", $1)})
    print("deviceToken: ", deviceTokenString)
    AppState.shared.deviceToken = deviceTokenString
  }
  
  func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
    print("didFailToRegisterForRemoteNotificationsWithError: ", error)
  }
  
  func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification) async -> UNNotificationPresentationOptions {
    print("willPresent:", notification.request.content.userInfo)
    Task {
      // 收到推送后, 刷新本地消息列表
      let messageItems = try await HttpRequest.getMessages().messages
      try MessageModel.saveAndUpdate(messageItems: messageItems)
    }
    return [.sound, .list, .banner]
  }
  
  func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse) async {
    print("didReceive:", response.notification.request.content.userInfo)
  }
  
}
