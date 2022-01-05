//
//  MessageListView.swift
//  PushDeer
//
//  Created by HEXT on 2021/12/25.
//

import SwiftUI

/// 消息界面
struct MessageListView: View {
  
  @State private var messages = Array(0..<10)
  @State private var isShowTest = true
  
  var body: some View {
    BaseNavigationView(title: "消息") {
      ScrollView {
        LazyVStack(alignment: .leading) {
          if isShowTest {
            TestPushView()
          }
          ForEach(messages, id: \.self) { msg in
            MessageItemView {
              messages.removeAll { _msg in
                _msg == msg
              }
            }
          }
          Spacer(minLength: 30)
        }
      }
      .navigationBarItems(trailing: Button(action: {
        withAnimation(.easeOut) {
          isShowTest = !isShowTest
        }
      }, label: {
        Image(systemName: isShowTest ? "chevron.up" : "chevron.down")
          .foregroundColor(Color(UIColor.lightGray))
      }))
    }
  }
}

struct TestPushView: View {
  @State private var testText = ""
  var body: some View {
    TextEditor(text: $testText)
      .overlay(RoundedRectangle(cornerRadius: 4).stroke(Color.accentColor))
      .frame(height: 128)
      .padding(EdgeInsets(top: 18, leading: 26, bottom: 0, trailing: 24))
    
    Button("推送测试") {
      print("点击推送测试")
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
