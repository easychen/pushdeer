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
        .environmentObject(store)
        .environment(\.managedObjectContext, persistenceController.container.viewContext)
    }
  }
}
