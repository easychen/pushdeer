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
  
  var body: some View {
    BaseNavigationView(title: "消息") {
      ScrollView {
        LazyVStack(alignment: .leading) {
          if store.isShowTestPush {
            TestPushView()
          }
          ForEach(store.messages) { messageItem in
            MessageItemView(messageItem: messageItem) {
              store.messages.removeAll { _messageItem in
                _messageItem.id == messageItem.id
              }
              HToast.showSuccess("已删除")
              Task {
                do {
                  _ = try await HttpRequest.rmMessage(id: messageItem.id)
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
        store.messages = try await HttpRequest.getMessages().messages
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
        HToast.showError("推送失败, 请先输入推送内容")
        return
      }
      Task {
        if store.keys.isEmpty {
          store.keys = try await HttpRequest.getKeys().keys
        }
        if let keyItem = store.keys.first {
          _ = try await HttpRequest.push(pushkey: keyItem.key, text: testText, desp: "", type: "")
          testText = ""
          HToast.showSuccess("推送成功")
          let messages = try await HttpRequest.getMessages().messages
          withAnimation(.easeOut) {
            store.messages = messages
          }
        } else {
          HToast.showError("推送失败, 请先添加一个Key")
        }
      }
    }
    .font(.system(size: 20))
    .frame(width: 104, height: 42)
    .foregroundColor(Color.white)
    .background(Color.accentColor)
    .cornerRadius(8)
    .padding(EdgeInsets(top: 12, leading: 26, bottom: 0, trailing: 24))
  }
}

struct MessageView_Previews: PreviewProvider {
  static var previews: some View {
    MessageListView()
  }
}
