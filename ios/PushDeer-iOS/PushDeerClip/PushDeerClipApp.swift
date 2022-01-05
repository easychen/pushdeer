//
//  PushDeerClipApp.swift
//  PushDeerClip
//
//  Created by HEXT on 2021/12/30.
//

import SwiftUI

@main
struct PushDeerClipApp: App {
  @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
  
  var body: some Scene {
    WindowGroup {
      ContentView().environmentObject(AppState.shared)
    }
  }
}
