//
//  DeletableView.swift
//  PushDeer
//
//  Created by HEXT on 2021/12/26.
//

import SwiftUI

/// 一个可左滑删除的包装View
struct DeletableView<Content : View> : View {
  /// 内容View
  @ViewBuilder let contentView: Content
  /// 删除按钮点击的回调
  let deleteAction : () -> ()
  
  /// 最大偏移 x, 可以认为是删除按钮漏出来的宽度
  private let offsetMaxX = 80.0
  
  @State private var offsetX = 0.0
  @State private var isShowDelete = false
  
  var body: some View {
    ZStack {
      HStack {
        Spacer()
        Button(action: {
          print("点击删除")
          withAnimation(.easeOut) {
            deleteAction()
          }
        }, label: {
          Image(systemName: "trash")
            .imageScale(.large)
            .foregroundColor(Color.red)
            .padding()
        })
      }
      contentView
        .offset(x: offsetX, y: 0)
        .gesture(
          DragGesture()
            .onChanged({ value in
              let width = value.translation.width
              print("onChanged", width)
              let endX = isShowDelete ? offsetMaxX : 0.0
              if width < endX {
                offsetX = width - endX
              } else {
                offsetX = 0
              }
            })
            .onEnded({ value in
              withAnimation(.easeOut) {
                let width = value.translation.width
                print("onEnded", width)
                if width > -(offsetMaxX/2) {
                  offsetX = 0
                  isShowDelete = false
                } else {
                  offsetX = -offsetMaxX
                  isShowDelete = true
                }
              }
            })
        )
    }
  }
}

struct DeletableView_Previews: PreviewProvider {
  static var previews: some View {
    DeletableView(contentView: {
      DeviceItemView(name: "未知设备")
    }, deleteAction: {
      
    })
  }
}
