//
//  KeyItemView.swift
//  PushDeer
//
//  Created by HEXT on 2021/12/27.
//

import SwiftUI

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
        Text("Key \(keyItem.id)")
          .font(.system(size: 20))
          .foregroundColor(Color.accentColor)
        Spacer()
        Image(systemName: "calendar")
          .font(.system(size: 14))
          .foregroundColor(Color.gray)
        Text("2021/12/01")
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
        }
        .font(.system(size: 20))
        .frame(width: 90, height: 42)
        .overlay(RoundedRectangle(cornerRadius: 4).stroke())
        .foregroundColor(Color.accentColor)
        
        Spacer()
        
        Button("复制") {
          print("点击复制")
          UIPasteboard.general.string = keyItem.key
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
    KeyItemView(keyItem: KeyItem(id: 1, key: "Key"))
  }
}
