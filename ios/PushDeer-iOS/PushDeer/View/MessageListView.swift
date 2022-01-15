//
//  MessageListView.swift
//  PushDeer
//
//  Created by HEXT on 2021/12/25.
//

import SwiftUI

/// 消息界面
struct MessageListView: View {
  @EnvironmentObject private var store: AppState
  @Environment(\.managedObjectContext) private var viewContext
  
  @FetchRequest(sortDescriptors: [NSSortDescriptor(keyPath: \MessageModel.created_at, ascending: false)], animation: .default)
  private var messages: FetchedResults<MessageModel>

  var body: some View {
    BaseNavigationView(title: "消息") {
      ScrollView {
        LazyVStack(alignment: .leading) {
          if store.isShowTestPush {
            TestPushView()
          }
          ForEach(messages) { messageItem in
            MessageItemView(messageItem: messageItem) {
              let id = messageItem.id
              viewContext.delete(messageItem)
              try? viewContext.save()
              HToast.showSuccess(NSLocalizedString("已删除", comment: "删除设备/Key/消息时提示"))
              Task {
                do {
                  _ = try await HttpRequest.rmMessage(id: Int(id))
                } catch {
                  
                }
              }
            }
          }
          Spacer(minLength: 30)
        }
      }
      .navigationBarItems(trailing: Button(action: {
        withAnimation(.easeOut) {
          store.isShowTestPush = !store.isShowTestPush
        }
      }, label: {
        Image(systemName: store.isShowTestPush ? "chevron.up" : "chevron.down")
          .foregroundColor(Color(UIColor.lightGray))
      }))
    }
    .onAppear {
      Task {
        let messageItems = try await HttpRequest.getMessages().messages
        try MessageModel.saveAndUpdate(messageItems: messageItems)
      }
    }
  }
}

struct TestPushView: View {
  @EnvironmentObject private var store: AppState
  @State private var testText = ""
  var body: some View {
    TextEditor(text: $testText)
      .overlay(RoundedRectangle(cornerRadius: 4).stroke(Color.accentColor))
      .frame(height: 128)
      .padding(EdgeInsets(top: 18, leading: 26, bottom: 0, trailing: 24))
    
    Button("推送测试") {
      print("点击推送测试")
      if testText.isEmpty {
        HToast.showError(NSLocalizedString("推送失败, 请先输入推送内容", comment: ""))
        return
      }
      Task {
        if store.keys.isEmpty {
          store.keys = try await HttpRequest.getKeys().keys
        }
        if let keyItem = store.keys.first {
          _ = try await HttpRequest.push(pushkey: keyItem.key, text: testText, desp: "", type: "")
          testText = ""
          HToast.showSuccess(NSLocalizedString("推送成功", comment: ""))
          let messageItems = try await HttpRequest.getMessages().messages
          withAnimation(.easeOut) {
            try? MessageModel.saveAndUpdate(messageItems: messageItems)
          }
        } else {
          HToast.showError(NSLocalizedString("推送失败, 请先添加一个Key", comment: ""))
        }
      }
    }
    .font(.system(size: 20))
    .frame(width: 104, height: 42)
    .foregroundColor(Color.white)
    .background(Color("BtnBgColor"))
    .cornerRadius(8)
    .padding(EdgeInsets(top: 12, leading: 26, bottom: 0, trailing: 24))
  }
}

struct MessageView_Previews: PreviewProvider {
  static var previews: some View {
    MessageListView()
  }
}
