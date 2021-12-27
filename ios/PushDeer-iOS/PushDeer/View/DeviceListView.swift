//
//  DeviceListView.swift
//  PushDeer
//
//  Created by HEXT on 2021/12/25.
//

import SwiftUI

/// 设备界面
struct DeviceListView: View {
  @State var devices = Array(0..<10)
  var body: some View {
    BaseNavigationView(title: "设备") {
      ScrollView {
        LazyVStack(alignment: .center) {
          ForEach(devices, id: \.self) { name in
            DeletableView(contentView: {
              DeviceItemView(name: "设备 \(name)")
            }, deleteAction: {
              devices.removeAll { _name in
                _name == name
              }
            })
              .padding(EdgeInsets(top: 18, leading: 26, bottom: 0, trailing: 24))
          }
        }
      }
      .navigationBarItems(trailing: Button(action: {
        withAnimation(.easeOut) {
          devices.insert(Int(arc4random_uniform(1000)), at: 0)
        }
      }, label: {
        Image(systemName: "plus")
          .foregroundColor(Color(UIColor.lightGray))
      }))
    }
  }
}

struct DeviceView_Previews: PreviewProvider {
  static var previews: some View {
    DeviceListView()
  }
}
