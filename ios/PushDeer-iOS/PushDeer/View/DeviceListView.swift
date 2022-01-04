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
  var body: some View {
    BaseNavigationView(title: "设备") {
      ScrollView {
        LazyVStack(alignment: .center) {
          ForEach(store.devices, id: \.id) { deviceItem in
            DeletableView(contentView: {
              DeviceItemView(name: deviceItem.name)
            }, deleteAction: {
              store.devices.removeAll { _deviceItem in
                _deviceItem.id == deviceItem.id
              }
            })
              .padding(EdgeInsets(top: 18, leading: 26, bottom: 0, trailing: 24))
          }
          Spacer(minLength: 30)
        }
      }
      .navigationBarItems(trailing: Button(action: {
        withAnimation(.easeOut) {
//          store.devices.insert(DeviceItem(), at: 0)
        }
      }, label: {
        Image(systemName: "plus")
          .foregroundColor(Color(UIColor.lightGray))
      }))
    }
    .onAppear {
      HttpRequest.getDevices()
    }
  }
}

struct DeviceView_Previews: PreviewProvider {
  static var previews: some View {
    DeviceListView()
  }
}
