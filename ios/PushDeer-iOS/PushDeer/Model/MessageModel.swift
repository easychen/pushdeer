//
//  MessageModel.swift
//  PushDeer
//
//  Created by HEXT on 2022/1/15.
//

import Foundation
import CoreData

extension MessageModel {
  convenience init(id: Int64, uid: String, text: String, desp: String, type: String, pushkey_name: String, created_at: String, context: NSManagedObjectContext = PersistenceController.shared.container.viewContext) {
    self.init(context: context)
    self.id = id
    self.uid = uid
    self.text = text
    self.desp = desp
    self.type = type
    self.pushkey_name = pushkey_name
    self.created_at = created_at
  }
  convenience init(messageItem: MessageItem, context: NSManagedObjectContext = PersistenceController.shared.container.viewContext) {
    self.init(
      id: Int64(messageItem.id),
      uid: messageItem.uid,
      text: messageItem.text,
      desp: messageItem.desp,
      type: messageItem.type,
      pushkey_name: messageItem.pushkey_name,
      created_at: messageItem.created_at,
      context: context)
  }
  
  var createdDateStr: String {
    dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ"
    let createdDate = dateFormatter.date(from: self.created_at ?? "") ?? Date()
    let timeInterval = -createdDate.timeIntervalSinceNow
    let minute = Int(floor(timeInterval / 60))
    if minute == 0 {
      return "刚刚"
    } else if minute <= 30 {
      return "\(minute)分钟前"
    } else if Calendar.current.isDateInToday(createdDate) {
      dateFormatter.dateFormat = "HH:mm:ss"
    } else {
      dateFormatter.dateFormat = "yyyy/MM/dd HH:mm:ss"
    }
    return dateFormatter.string(from: createdDate)
  }
  
  static let _viewContext = PersistenceController.shared.container.viewContext
  static let _fetchRequest = MessageModel.fetchRequest()
  
  /// 删除本地持久化的所有消息
  static func deleteAll() throws -> Void {
    //    let fetchRequest: NSFetchRequest<NSFetchRequestResult> = MessageModel.fetchRequest()
    //    let deleteRequest = NSBatchDeleteRequest(fetchRequest: fetchRequest)
    //    try _viewContext.execute(deleteRequest)
    let fetchRequest = MessageModel.fetchRequest()
    let models = try _viewContext.fetch(fetchRequest)
    models.forEach { model in
      _viewContext.delete(model)
    }
    try _viewContext.save()
  }
  
  /// 持久化保存和更新
  static func saveAndUpdate(messageItems: [MessageItem]) throws -> Void {
    try messageItems.forEach(saveAndUpdate)
  }
  
  /// 持久化保存和更新
  static func saveAndUpdate(messageItem: MessageItem) throws -> Void {
    _fetchRequest.predicate = NSPredicate(format: "id = \(messageItem.id)")
    let models = try _viewContext.fetch(_fetchRequest)
    if models.isEmpty {
      // 如果本地不存在, 就构建一个新的放进 context
      _ = MessageModel(messageItem: messageItem, context: _viewContext)
    } else {
      // 如果存在, 就更新第一个, 删除其它重复的
      models.enumerated().forEach { element in
        let messageModel = element.element
        let index = element.offset
        if index == 0 {
          messageModel.id = Int64(messageItem.id);
          messageModel.uid = messageItem.uid;
          messageModel.text = messageItem.text;
          messageModel.desp = messageItem.desp;
          messageModel.type = messageItem.type;
          messageModel.pushkey_name = messageItem.pushkey_name;
          messageModel.created_at = messageItem.created_at;
        } else {
          _viewContext.delete(messageModel)
        }
      }
    }
    // 保存 context 中的所有改动
    try _viewContext.save()
  }
}
