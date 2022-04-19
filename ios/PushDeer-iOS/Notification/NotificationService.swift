//
//  NotificationService.swift
//  Notification
//
//  Created by HEXT on 2022/4/19.
//

import UserNotifications
import WidgetKit

class NotificationService: UNNotificationServiceExtension {
  
  var contentHandler: ((UNNotificationContent) -> Void)?
  var bestAttemptContent: UNMutableNotificationContent?
  
  override func didReceive(_ request: UNNotificationRequest, withContentHandler contentHandler: @escaping (UNNotificationContent) -> Void) {
    self.contentHandler = contentHandler
    bestAttemptContent = (request.content.mutableCopy() as? UNMutableNotificationContent)
    
    NSLog("push-userInfo: %@", bestAttemptContent?.userInfo ?? "")
    // 刷新所有桌面小部件
    WidgetCenter.shared.reloadAllTimelines()
    
    if let bestAttemptContent = bestAttemptContent {
      // Modify the notification content here...
      // bestAttemptContent.title = "\(bestAttemptContent.title) [modified]"
      
      contentHandler(bestAttemptContent)
    }
  }
  
  override func serviceExtensionTimeWillExpire() {
    // Called just before the extension will be terminated by the system.
    // Use this as an opportunity to deliver your "best attempt" at modified content, otherwise the original push payload will be used.
    if let contentHandler = contentHandler, let bestAttemptContent =  bestAttemptContent {
      contentHandler(bestAttemptContent)
    }
  }
  
}
