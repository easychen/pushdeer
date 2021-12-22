//
//  PushDeerApp.swift
//  PushDeer
//
//  Created by Easy on 2021/11/30.
//

import SwiftUI
import UserNotifications

@main
struct PushDeerApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    
    // 初始化 store 对象，传递到环境中
    @StateObject private var store = AppState()
    
    var body: some Scene {
        WindowGroup {
            ContentView().environmentObject(store).onAppear(perform: {
                // appDelegate.device_token = "888"
                // print( AppDelegate.device_token + "pushdeer")
            })
        }
    }
}
