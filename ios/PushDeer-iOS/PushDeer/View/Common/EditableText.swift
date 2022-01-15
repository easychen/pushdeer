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
      textField()
        .submitLabel(.done)
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
