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
              HToast.showSuccess("已删除")
              Task {
                do {
                  _ = try await HttpRequest.rmKey(id: keyItem.id)
                } catch {
                  
                }
              }
            })
              .padding(EdgeInsets(top: 18, leading: 26, bottom: 0, trailing: 24))
          }
          Spacer(minLength: 30)
        }
      }
      .navigationBarItems(trailing: Button(action: {
        Task {
          let keys = try await HttpRequest.genKey().keys
          withAnimation(.easeOut) {
            store.keys = keys
          }
          HToast.showSuccess("已添加新Key")
        }
      }, label: {
        Image(systemName: "plus")
          .foregroundColor(Color(UIColor.lightGray))
      }))
    }
    .onAppear {
      HttpRequest.loadKeys()
    }
  }
}

struct KeyView_Previews: PreviewProvider {
  static var previews: some View {
    KeyListView().environmentObject(AppState.shared)
  }
}
