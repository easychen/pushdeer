//
//  AppDelegate.swift
//  PushDeer
//
//  Created by HeXiaoTian on 2021/12/31.
//

import UIKit
import UserNotifications

class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate {
  
  static var deviceToken: String = ""
  
  func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
    
    let center = UNUserNotificationCenter.current()
    center.delegate = self
    center.requestAuthorization(options: [.badge, .sound, .alert]) { granted, error in
      print("注册通知结果: \(granted) - \(String(describing: error))")
    }
    application.registerForRemoteNotifications()
    
    Task {
      // APP启动后要先调一个无用接口, 用于触发国行手机的网络授权弹框, 未授权前调的接口会直接失败. (提前触发网络授权弹窗)
      let result = try await HttpRequest.fake()
      AppState.shared.token = result.token
      HttpRequest.getDevices()
    }
    
    return true
  }
  
  func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
    let deviceTokenString = deviceToken.reduce("", {$0 + String(format: "%02X", $1)})
    print("deviceToken: ", deviceTokenString)
    AppDelegate.deviceToken = deviceTokenString;
  }
  
  func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification) async -> UNNotificationPresentationOptions {
    print("willPresent:", notification.request.content.userInfo)
    return [.sound, .list, .banner]
  }
  
  func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse) async {
    print("didReceive:", response.notification.request.content.userInfo)
  }
  
}
