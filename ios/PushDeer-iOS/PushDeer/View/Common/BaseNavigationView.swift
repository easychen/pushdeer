//
//  BaseNavigationView.swift
//  PushDeer
//
//  Created by HEXT on 2021/12/26.
//

import SwiftUI

/// 具有导航栏结构的基础容器View, APP内的页面基本上都可以使用它包装
struct BaseNavigationView<Content : View> : View {
  /// 导航栏标题
  let title: LocalizedStringKey
  /// 页面主体View
  @ViewBuilder let contentView: Content
  
  @Environment(\.colorScheme) private var colorScheme
  
  var body: some View {
    NavigationView {
      ZStack {
        // VStack HStack Spacer 组合起来撑到最大
        VStack {
          HStack {
            Spacer()
          }
          Spacer()
        }
        contentView
      }
      .background(
        Image("deer.gray")
          .offset(x: -150, y: -10)
          .opacity(colorScheme == .dark ? 0.4 : 1),
        alignment: .bottom
      )
      .navigationBarTitle(title)
    }
    .navigationViewStyle(.stack)
  }
}

struct BaseNavigationView_Previews: PreviewProvider {
  static var previews: some View {
    BaseNavigationView(title: "标题") {
      Text("内容")
    }
    .environment(\.colorScheme, .dark)
  }
}
