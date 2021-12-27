//
//  DeviceItemView.swift
//  PushDeer
//
//  Created by HEXT on 2021/12/26.
//

import SwiftUI

/// 每个设备项的 View
struct DeviceItemView: View {
  var name: String
  var body: some View {
    CardView {
      HStack{
        Image(systemName: "ipad.and.iphone")
          .resizable()
          .scaledToFit()
          .frame(width: 40, height: 40, alignment: .center)
          .padding(EdgeInsets(top: 0, leading: 18, bottom: 0, trailing: 8))
        Text(name)
          .font(.system(size: 20))
        Spacer()
      }
      .frame(height: 80)
    }
  }
}

struct DeviceItemView_Previews: PreviewProvider {
  static var previews: some View {
    DeviceItemView(name: "未知设备")
  }
}
