//
//  MessageItemView.swift
//  PushDeer
//
//  Created by HEXT on 2021/12/29.
//

import SwiftUI

struct MessageItemView: View {
  /// 删除按钮点击的回调
  let deleteAction : () -> ()
  
  var body: some View {
    VStack {
      HStack {
        HLine().stroke(Color(UIColor.lightGray))
          .frame(width: 20, height: 1)
        Image("avatar2")
          .resizable()
          .scaledToFit()
          .frame(width: 38, height: 38)
        Text("key名字")
          .font(.system(size: 14))
          .foregroundColor(Color(UIColor.darkGray))
        Text("· 5分钟前")
          .font(.system(size: 12))
          .foregroundColor(Color(UIColor.darkGray))
        HLine().stroke(Color(UIColor.lightGray))
          .frame(height: 1)
      }
      
      DeletableView(contentView: {
        CardView {
          HStack{
            Text("纯文本的效果")
              .font(.system(size: 14))
              .foregroundColor(Color(UIColor.darkGray))
              .padding()
            Spacer(minLength: 0)
          }
          .contextMenu {
            Button("复制") {
              UIPasteboard.general.string = "someText"
            }
          }
        }
      }, deleteAction: deleteAction)
        .padding(EdgeInsets(top: 10, leading: 26, bottom: 0, trailing: 24))
      
    }
    .padding(.top, 25)
  }
}

struct MessageItemView_Previews: PreviewProvider {
  static var previews: some View {
    VStack {
      MessageItemView(){}
      MessageItemView(){}
      Spacer()
    }
  }
}
