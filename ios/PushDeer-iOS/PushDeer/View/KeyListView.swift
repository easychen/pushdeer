//
//  KeyListView.swift
//  PushDeer
//
//  Created by HEXT on 2021/12/25.
//

import SwiftUI

struct KeyItem: Codable, Identifiable{
  let id: Int
  let key: String
}

/// Key 界面
struct KeyListView: View {
  @State private var keyItems = [
    KeyItem(id: 1, key: UUID().uuidString),
    KeyItem(id: 2, key: UUID().uuidString),
    KeyItem(id: 3, key: UUID().uuidString),
    KeyItem(id: 4, key: UUID().uuidString),
  ]
  var body: some View {
    BaseNavigationView(title: "Key") {
      ScrollView {
        LazyVStack(alignment: .center) {
          ForEach(keyItems) { keyItem in
            DeletableView(contentView: {
              CardView {
                KeyItemView(keyItem: keyItem)
              }
              
            }, deleteAction: {
              keyItems.removeAll { _keyItem in
                keyItem.id == _keyItem.id
              }
            })
              .padding(EdgeInsets(top: 18, leading: 26, bottom: 0, trailing: 24))
          }
        }
      }
      .navigationBarItems(trailing: Button(action: {
        let keyItem = KeyItem(id: Int(arc4random_uniform(1000)), key: UUID().uuidString)
        withAnimation(.easeOut) {
          keyItems.insert(keyItem, at: 0)
        }
      }, label: {
        Image(systemName: "plus")
          .foregroundColor(Color(UIColor.lightGray))
      }))
    }
  }
}

struct KeyView_Previews: PreviewProvider {
  static var previews: some View {
    KeyListView()
  }
}
