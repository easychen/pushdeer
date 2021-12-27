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
  let title: String
  /// 页面主体View
  @ViewBuilder let contentView: Content
  
  var body: some View {
    NavigationView {
      ZStack {
        Spacer()
          .frame(width: .infinity, height: .infinity)
        contentView
      }
      .background(
        Image("deer.gray").offset(x: -150, y: -10),
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
  }
}
