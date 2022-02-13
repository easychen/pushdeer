//
//  ViewExtension.swift
//  PushDeer
//
//  Created by HEXT on 2022/2/12.
//

import SwiftUI

/// 给 View 加扩展, 包括给系统扩展包一层以兼容老系统
extension View {
  
  /*
   ps: #available(iOS 15.0, *) 判断在 Xcode 13.2 上面有 bug
   https://stackoverflow.com/questions/70506330/swiftui-app-crashes-with-different-searchbar-viewmodifier-on-ios-14-15/70603710#70603710
   例如: if #available(iOS 15.0, *) { textField().submitLabel(.done) }
   比如就算在 iOS 14 上, 也会寻找 submitLabel 方法,
   submitLabel 方法在 iOS 14 上 不存在, 就会造成奔溃.
   一般解决方法为多包一层, 比如用自己的 ViewModifier 包装系统的, 再去判断
   比如自己写个 aaa ViewModifier 去包装系统的 submitLabel, 然后使用的地方改成自己的方法:
   if #available(iOS 15.0, *) { textField().aaa(.done) }
   自己写的 aaa ViewModifier 方法在每个系统上都可以找到, 就不会奔溃了
   */
  
  /// 给 List 添加 下拉刷新
  func refresh(action: @escaping @Sendable () async -> Void) -> some View {
    Group {
      if #available(iOS 15.0, *) {
        self.modifier(RefreshModifier(action: action))
      } else {
        self
      }
    }
  }
  
  /// 键盘回车按钮的文字显示为: 完成/Done
  func submitLabelDone() -> some View {
    Group {
      if #available(iOS 15.0, *) {
        self.modifier(SubmitLabelDoneModifier())
      } else {
        self
      }
    }
  }
}

@available(iOS 15.0, *)
struct RefreshModifier: ViewModifier {
  let action: @Sendable () async -> Void
  func body(content: Content) -> some View {
    content
      .refreshable(action: action)
  }
}

@available(iOS 15.0, *)
struct SubmitLabelDoneModifier: ViewModifier {
  func body(content: Content) -> some View {
    content
      .submitLabel(.done)
  }
}

struct ViewExtension_Previews: PreviewProvider {
  static var previews: some View {
    List {
      Text("A List Item")
      Text("A Second List Item")
      Text("A Third List Item")
      TextField("请输入", text: .constant(""))
        .submitLabelDone()
    }
    .refresh {
      
    }
  }
}
