//
//  ContentView.swift
//  PushDeer
//
//  Created by HEXT on 2021/12/25.
//

import SwiftUI

struct ContentView: View {
  
  @EnvironmentObject private var store: AppState
  
  var body: some View {
    if store.token.isEmpty {
      LoginView()
    } else {
      MainView()
    }
  }
}

struct ContentView_Previews: PreviewProvider {
  static var previews: some View {
    ContentView()
  }
}
