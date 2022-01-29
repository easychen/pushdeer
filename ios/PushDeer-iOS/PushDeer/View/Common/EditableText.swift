//
//  EditableText.swift
//  PushDeer
//
//  Created by HEXT on 2022/1/10.
//

import SwiftUI

struct EditableText: View {
  var placeholder = ""
  @State var value = ""
  var onCommit: (_ value: String) -> Void = { value in
    
  }
  
  func textField() -> some View {
    TextField(placeholder, text: $value, onCommit: {
      print("修改文本:", value)
      self.onCommit(value)
    })
      .font(.system(size: 20))
      .foregroundColor(Color.accentColor)
  }
  
  var body: some View {
    if #available(iOS 15.0, *) {
      Group {
        // https://stackoverflow.com/questions/70506330/swiftui-app-crashes-with-different-searchbar-viewmodifier-on-ios-14-15/70603710#70603710
        // !!!: 用 Group 包起来, 并且再次检查, 是因为在 Xcode 13.2 上面有 bug, 造成老版本也会寻找 if 里面的新方法, 造成老版本奔溃.
        if #available(iOS 15.0, *) {
          textField()
            .submitLabel(.done)
        }
      }
    } else {
      textField()
    }
  }
}

struct EditableText_Previews: PreviewProvider {
  static var previews: some View {
    VStack {
      EditableText(value: "你好")
      EditableText(placeholder: "请输入")
      EditableText()
      EditableText(placeholder: "请输入") { value in
        
      }
    }
  }
}
