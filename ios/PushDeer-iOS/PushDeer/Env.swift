//
//  Env.swift
//  PushDeer
//
//  Created by HEXT on 2022/1/30.
//

import Foundation

/// APP运行环境
struct Env {
  /// 是否是自建服务, 自建服务为 true, 在线服务为 false
  static let isSelfHosted: Bool = {
    #if SELFHOSTED
    return true
    #else
    return false
    #endif
  }()
  /// 在线版本的 Api Endpoint
  static let onlineApiEndpoint = "https://api2.pushdeer.com"
  /// AppStore 的 appId, 自建版: 1608017631; 在线版: 1596771139
  static let appStoreId = isSelfHosted ? 1608017631 : 1596771139
  /// 微信开发者ID
  static let wxAppid = "wx3ae07931d0555a24"
  /// 微信开发者Universal Link
  static let wxUniversalLink = "https://vip.pushdeer.com/app/"
  /// PushDeer 官网地址
  static let officialWebsite = "https://www.pushdeer.com"
  /// 共享组名, 使用它来访问 App Clip Widget 共享数据
  static let appGroupId: String = {
    #if SELFHOSTED
    return "group.com.pushdeer.self.ios"
    #else
    return "group.com.pushdeer.app.ios"
    #endif
  }()
  
}
