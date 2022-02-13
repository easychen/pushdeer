//
//  DeviceListView.swift
//  PushDeer
//
//  Created by HEXT on 2021/12/25.
//

import SwiftUI

/// 设备界面
struct DeviceListView: View {
  @EnvironmentObject private var store: AppState
  @State private var isShowAlert = false
  var body: some View {
    BaseNavigationView(title: "设备") {
      ScrollView {
        LazyVStack(alignment: .center) {
          ForEach(store.devices.reversed()) { deviceItem in
            DeletableView(contentView: {
              DeviceItemView(deviceItem: deviceItem)
            }, deleteAction: {
              store.devices.removeAll { _deviceItem in
                _deviceItem.id == deviceItem.id
              }
              // 尝试触发 @Published, 看能不能解决 UI 偶尔不更新的问题
              store.devices = store.devices
              HToast.showSuccess(NSLocalizedString("已删除", comment: "删除设备/Key/消息时提示"))
              Task {
                do {
                  _ = try await HttpRequest.rmDevice(id: deviceItem.id)
                } catch {
                  
                }
              }
            })
              .padding(EdgeInsets(top: 18, leading: 26, bottom: 0, trailing: 24))
          }
          
          if store.devices.isEmpty {
            Text("你还未注册当前设备, 注册后才能收到推送, 你可以点击右上角 \(Image(systemName: "plus")) 添加当前设备")
              .foregroundColor(Color(UIColor.lightGray))
              .padding()
          }
          
          Spacer(minLength: 30)
        }
      }
      .navigationBarItems(trailing: Button(action: regDevice, label: {
        Image(systemName: "plus")
          .foregroundColor(Color(UIColor.lightGray))
      }))
    }
    .onAppear {
      Task {
        // 加载已注册设备列表
        let result = try await HttpRequest.getDevices()
        AppState.shared.devices = result.devices
        
        // 首次提示添加设备
        let hasAlertRegDevice = UserDefaults.standard.bool(forKey: "PushDeer_hasAlertRegDevice")
        if !AppState.shared.deviceToken.isEmpty && !hasAlertRegDevice {
          let hasContains = result.devices.contains { deviceItem in
            deviceItem.device_id == AppState.shared.deviceToken
          }
          if !hasContains {
            isShowAlert = true
            UserDefaults.standard.set(true, forKey: "PushDeer_hasAlertRegDevice")
          }
        }
      }
    }
    .alert(isPresented: $isShowAlert) {
      Alert(
        title: Text("温馨提示"),
        message: Text("你还未注册当前设备, 注册后才能收到推送, 是否现在注册? (你还可以稍后点击右上角 + 符号添加当前设备)"),
        primaryButton: .default(
          Text("注册"),
          action: regDevice
        ),
        secondaryButton: .cancel(Text("稍后"))
      )
    }
  }
  
  func regDevice() -> Void {
    Task {
      let hasContains = store.devices.contains { store.deviceToken == $0.device_id }
      if hasContains {
        HToast.showInfo(NSLocalizedString("已添加过当前设备", comment: ""))
        return;
      }
      let devices = try await HttpRequest.regDevice().devices
      withAnimation(.easeOut) {
        store.devices = devices
      }
      HToast.showSuccess(NSLocalizedString("已添加当前设备", comment: ""))
    }
  }
}

struct DeviceView_Previews: PreviewProvider {
  static var previews: some View {
    DeviceListView()
      .environmentObject(AppState.shared)
      .environment(\.colorScheme, .dark)
  }
}
