//
//  ListTest.swift
//  PushDeer
//
//  Created by HEXT on 2022/2/13.
//

import SwiftUI

/// 这是一个测试代码, 测试APP中几个列表改成List的可行性, 最后证明在iOS 15以上勉强可以, iOS 14 不太行, 样式达不到UI标准
struct ListTestView: View {
  var body: some View {
    List {
      Text("Hello, world!1")
      Text("Hello, world!2")
        .listRowBackground(Color.red)
      Text("Hello, world!3")
      
      ForEach.init([
        DeviceItem(id: 0, uid: "", name: "Hext's iPhone 11", type: "", device_id: "", is_clip: 1)
      ]) {
        DeviceItemView(deviceItem: $0)
      }
      .onDelete(perform: { indexSet in
        HToast.showSuccess("删除成功")
      })
//      .listRowSeparator(.hidden)
//      .listRowBackground(Color.red)
      
    }
    .onAppear(perform: {
      UITableView.appearance().backgroundColor = UIColor.clear
      UITableView.appearance().separatorStyle = .none
      UITableView.appearance().separatorColor = UIColor.clear
    })
    .background(Color.white)
    
    //      .listStyle(DefaultListStyle())
    //      .listStyle(BorderedListStyle()) //
    //      .listStyle(CarouselListStyle()) //
    //      .listStyle(EllipticalListStyle()) //
    //      .listStyle(GroupedListStyle())
    //      .listStyle(InsetListStyle())
    //      .listStyle(InsetGroupedListStyle())
    .listStyle(PlainListStyle())
    //      .listStyle(SidebarListStyle())
    
    .refresh {
      HToast.showSuccess("刷新成功")
    }
  }
}

struct ListTestView_Previews: PreviewProvider {
  static var previews: some View {
    ListTestView()
      .environmentObject(AppState.shared)
  }
}
