//
//  DeviceItemView.swift
//  PushDeer
//
//  Created by HEXT on 2021/12/26.
//

import SwiftUI

/// 每个设备项的 View
struct DeviceItemView: View {
  let deviceItem: DeviceItem
  @EnvironmentObject private var store: AppState
  
  var body: some View {
    CardView {
      HStack{
        ZStack{
          Image(systemName: getSystemName(deviceName: deviceItem.name))
            .resizable()
            .scaledToFit()
            .frame(width: 40, height: 40, alignment: .center)
          if deviceItem.is_clip == 1 {
            Image(systemName: "appclip")
              .resizable()
              .scaledToFit()
              .frame(width: 12, height: 12, alignment: .center)
          }
        }
        .padding(EdgeInsets(top: 0, leading: 18, bottom: 0, trailing: 8))
        
        EditableText(placeholder: NSLocalizedString("输入设备名称", comment: ""), value: deviceItem.name) { value in
          Task {
            // 调用接口修改
            _ = try await HttpRequest.renameDevice(id: deviceItem.id, name: value)
            HToast.showSuccess(NSLocalizedString("已修改设备名称", comment: ""))
            // 在此 Item 在列表中的下标
            let index = store.devices.firstIndex { $0.id == deviceItem.id }
            if let index = index {
              // 更新列表中相应的 Item
              store.devices[index].name = value
            }
          }
        }
        Text(getInfo(deviceItem: deviceItem))
          .font(.system(size: 20))
        Spacer()
      }
      .frame(height: 80)
    }
  }
  
  func getInfo(deviceItem: DeviceItem) -> String {
    var name = ""
    //    if deviceItem.is_clip == 1 {
    //      name += " [Clip]"
    //    }
    if deviceItem.device_id == store.deviceToken {
      name += NSLocalizedString("(当前设备)", comment: "在设备列表中标识当前设备")
    }
    return name
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
    DeviceItemView(deviceItem: DeviceItem(id: 0, uid: "", name: "Hext's iPhone 11", type: "", device_id: "", is_clip: 1))
      .environmentObject(AppState.shared)
  }
}
