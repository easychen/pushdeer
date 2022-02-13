//
//  KeyListView.swift
//  PushDeer
//
//  Created by HEXT on 2021/12/25.
//

import SwiftUI

/// Key 界面
struct KeyListView: View {
  @EnvironmentObject private var store: AppState
  var body: some View {
    BaseNavigationView(title: "Key") {
      ScrollView {
        LazyVStack(alignment: .center) {
          ForEach(store.keys.reversed()) { keyItem in
            DeletableView(contentView: {
              CardView {
                KeyItemView(keyItem: keyItem)
              }
              
            }, deleteAction: {
              store.keys.removeAll { _keyItem in
                keyItem.id == _keyItem.id
              }
              // 尝试触发 @Published, 看能不能解决 UI 偶尔不更新的问题
              store.keys = store.keys
              HToast.showSuccess(NSLocalizedString("已删除", comment: "删除设备/Key/消息时提示"))
              Task {
                do {
                  _ = try await HttpRequest.rmKey(id: keyItem.id)
                } catch {
                  
                }
              }
            })
              .padding(EdgeInsets(top: 18, leading: 26, bottom: 0, trailing: 24))
          }
          
          if store.keys.isEmpty {
            Text("你还未添加过 Key, 发送推送需要使用 Key, 你可以点击右上角 \(Image(systemName: "plus")) 生成一个 Key")
              .foregroundColor(Color(UIColor.lightGray))
              .padding()
          }
          
          Spacer(minLength: 30)
        }
      }
      .navigationBarItems(trailing: Button(action: genKey, label: {
        Image(systemName: "plus")
          .foregroundColor(Color(UIColor.lightGray))
      }))
    }
    .onAppear {
      Task {
        let result = try await HttpRequest.getKeys()
        AppState.shared.keys = result.keys
        
        // 首次自动生成一个 key
        let hasAlertGenKey = UserDefaults.standard.bool(forKey: "PushDeer_hasAlertGenKey")
        if result.keys.isEmpty && !hasAlertGenKey {
          genKey()
          UserDefaults.standard.set(true, forKey: "PushDeer_hasAlertGenKey")
        }
      }
    }
  }
  
  func genKey() -> Void {
    Task {
      let keys = try await HttpRequest.genKey().keys
      withAnimation(.easeOut) {
        store.keys = keys
      }
      HToast.showSuccess(NSLocalizedString("已添加新Key", comment: ""))
    }
  }
}

struct KeyView_Previews: PreviewProvider {
  static var previews: some View {
    KeyListView().environmentObject(AppState.shared)
  }
}
