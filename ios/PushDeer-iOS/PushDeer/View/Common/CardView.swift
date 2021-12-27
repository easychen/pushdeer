//
//  CardView.swift
//  PushDeer
//
//  Created by HEXT on 2021/12/26.
//

import SwiftUI

/// 一个通用的卡片包装View
struct CardView<Content : View> : View {
  /// 内容View
  @ViewBuilder let contentView: Content
  
  var body: some View {
    contentView
      .overlay(RoundedRectangle(cornerRadius: 8).stroke())
      .foregroundColor(Color.accentColor)
      .background(
        Color(UIColor.systemBackground)
          .cornerRadius(8)
          .shadow(
            color: Color.black.opacity(0.16),
            radius: 6, x: 0, y: 3
          )
      )
  }
}

struct CardView_Previews: PreviewProvider {
  static var previews: some View {
    CardView {
      Text("Hello, World!")
        .padding()
    }
  }
}
