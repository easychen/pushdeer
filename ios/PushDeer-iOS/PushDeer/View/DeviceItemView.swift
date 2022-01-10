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
        Image(systemName: getSystemName(deviceName: name))
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
  
  func getSystemName(deviceName: String) -> String {
    var deviceName = deviceName.lowercased()
    deviceName = deviceName.replacingOccurrences(of: " ", with: "")
    
//    if deviceName.contains("clip") {
//      return "appclip"
//    }
    if deviceName.contains("iphone") {
      return "iphone"
    }
    if deviceName.contains("ipad") {
      return "ipad.landscape"
    }
    if deviceName.contains("macbook") {
      return "laptopcomputer"
    }
    if deviceName.contains("imac") {
      return "desktopcomputer"
    }
    if deviceName.contains("macpro") {
      return "macpro.gen3"
    }
    if deviceName.contains("macmini") {
      return "macmini"
    }
    if deviceName.contains("mac") {
      return "macwindow"
    }
    return "ipad.and.iphone"
  }
}

struct DeviceItemView_Previews: PreviewProvider {
  static var previews: some View {
    DeviceItemView(name: "未知设备")
  }
}
