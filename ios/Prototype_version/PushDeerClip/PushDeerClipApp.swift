//
//  PushDeerClipApp.swift
//  PushDeerClip
//
//  Created by Easy on 2021/12/8.
//

import SwiftUI

@main
struct PushDeerClipApp: App {
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
