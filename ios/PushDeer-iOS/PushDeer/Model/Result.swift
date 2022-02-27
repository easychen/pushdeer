//
//  Result.swift
//  PushDeer
//
//  Created by HEXT on 2022/1/4.
//

import Foundation

/// 每个API接口的标准返回结构
struct ApiResult<T : Codable> : Codable{
  let code: Int
  /// 错误提示, code != 0 时才有
  let error: String?
  /// 返回内容, code == 0 时才有
  let content: T?
}

struct TokenContent: Codable{
  let token: String
}

struct UserInfoContent: Codable{
  let id: Int
  let name: String?
  let email: String?
  let apple_id: String?
  let wechat_id: String?
  let level: Int
  let created_at: String
  let updated_at: String
}

struct DeviceItem: Codable, Identifiable{
  let id: Int
  let uid: String
  var name: String
  let type: String
  let device_id: String
  let is_clip: Int
}

struct DeviceContent: Codable{
  let devices: [DeviceItem]
}

struct KeyContent: Codable{
  let keys: [KeyItem]
}

struct KeyItem: Codable, Identifiable{
  let id: Int
  var name: String
  let uid: String
  let key: String
  let created_at: String
}

struct MessageContent: Codable{
  let messages: [MessageItem]
}

struct MessageItem: Codable, Identifiable{
  let id: Int
  let uid: String
  let text: String
  let desp: String
  let type: String
  let pushkey_name: String
  let created_at: String
}

struct ActionContent: Codable{
  let message: String
}

struct ResultContent: Codable{
  let result: Array<String>
}

let dateFormatter = DateFormatter()

extension KeyItem {
  var createdDateStr: String {
    dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ"
    let createdDate = dateFormatter.date(from: self.created_at)
    dateFormatter.dateFormat = "yyyy/MM/dd"
    return dateFormatter.string(from: createdDate ?? Date())
  }
}
