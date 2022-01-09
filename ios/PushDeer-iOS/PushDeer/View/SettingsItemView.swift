//
//  SettingsItemView.swift
//  PushDeer
//
//  Created by HeXiaoTian on 2021/12/27.
//

import SwiftUI

/// 每个设置项的 View
struct SettingsItemView: View {
  let title: String
  let button: String
  let action: () -> ()
  var body: some View {
    CardView {
      HStack{
        Text(title)
          .font(.system(size: 18))
          .foregroundColor(Color(UIColor.darkGray))
          .padding(.leading, 16)
        Spacer(minLength: 0)
        Button(button) {
          print("点击\(button)")
          action()
        }
        .font(.system(size: 20))
        .frame(width: 80, height: 42)
        .foregroundColor(Color.white)
        .background(Color.accentColor)
        .cornerRadius(8)
        .padding()
      }
      .frame(height: 74)
    }
  }
}

struct SettingsItemView_Previews: PreviewProvider {
  static var previews: some View {
    SettingsItemView(title: "登录为 Hext", button: "退出") {
      // logout
    }
  }
}
