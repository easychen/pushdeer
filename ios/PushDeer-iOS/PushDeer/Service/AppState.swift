//
//  AppState.swift
//  PushDeer
//
//  Created by HEXT on 2022/1/4.
//

import Foundation
class AppState: ObservableObject {
  @Published var token : String? = nil
  @Published var devices: [DeviceItem] = []
  @Published var keys: [KeyItem] = []
  @Published var messages: [MessageItem] = []
  @Published var tab_selected: Int = 1
  @Published var device_token: String = ""
  
  static let shared = AppState()
  private init() {}
}
