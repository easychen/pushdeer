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
  
  var body: some Scene {
    WindowGroup {
      ContentView().environmentObject(AppState.shared)
    }
  }
}
