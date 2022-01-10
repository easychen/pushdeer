//
//  KeyItemView.swift
//  PushDeer
//
//  Created by HEXT on 2021/12/27.
//

import SwiftUI

struct KeyItemTextField: View {
  let keyItem: KeyItem
  @State private var value = ""
  @EnvironmentObject private var store: AppState
  
  init(keyItem: KeyItem) {
    self.keyItem = keyItem
    self._value = State(initialValue: keyItem.name)
  }
  
  func textField() -> some View {
    TextField("输入key名称", text: $value, onCommit: {
      Task {
        do {
          // 调用接口修改
          _ = try await HttpRequest.renameKey(id: keyItem.id, name: value)
          HToast.showSuccess("已修改key名称")
          // 在此 keyItem 在列表中的下标
          let index = store.keys.firstIndex { _keyItem in
            _keyItem.id == keyItem.id
          }
          if let index = index {
            let _keyItem = store.keys[index]
            // 更新列表中相应的 keyItem
            store.keys[index] = KeyItem(
              id: _keyItem.id,
              name: value,
              uid: _keyItem.uid,
              key: _keyItem.key,
              created_at: _keyItem.created_at
            )
          }
        } catch {
          
        }
      }
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

/// 每个 Key 项的 View
struct KeyItemView: View {
  let keyItem: KeyItem
  
  var body: some View {
    VStack(spacing: 20) {
      HStack(alignment: .bottom) {
        Image("avatar2")
          .resizable()
          .scaledToFit()
          .frame(width: 38, height: 38)
        KeyItemTextField(keyItem: keyItem)
        Spacer()
        Image(systemName: "calendar")
          .font(.system(size: 14))
          .foregroundColor(Color.gray)
        Text(keyItem.createdDateStr)
          .font(.system(size: 14))
          .foregroundColor(Color.gray)
      }
      
      TextField("Key", text: .constant(keyItem.key) )
        .font(.system(size: 14))
        .disabled(true)
        .padding(12)
        .overlay(RoundedRectangle(cornerRadius: 4).stroke(Color(UIColor.lightGray)))
        .foregroundColor(Color.gray)
      
      HLine().stroke(Color.gray, style: StrokeStyle(lineWidth: 1, dash: [5]))
      
      HStack {
        Button("重置") {
          print("点击重置")
          Task {
            do {
              _ = try await HttpRequest.regenKey(id: keyItem.id)
              HttpRequest.loadKeys()
              HToast.showSuccess("已重置")
            } catch {
              
            }
          }
        }
        .font(.system(size: 20))
        .frame(width: 90, height: 42)
        .overlay(RoundedRectangle(cornerRadius: 4).stroke())
        .foregroundColor(Color.accentColor)
        
        Spacer()
        
        Button("复制") {
          print("点击复制")
          UIPasteboard.general.string = keyItem.key
          HToast.showSuccess("已复制")
        }
        .font(.system(size: 20))
        .frame(width: 90, height: 42)
        .foregroundColor(Color.white)
        .background(Color.accentColor)
        .cornerRadius(8)
      }
    }
    .padding()
  }
}

struct KeyItemView_Previews: PreviewProvider {
  static var previews: some View {
    KeyItemView(keyItem: KeyItem(id: 1, name: "name", uid: "1", key: "Key", created_at: "1111"))
  }
}
