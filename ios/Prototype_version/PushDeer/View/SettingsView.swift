//
//  SettingsView.swift
//  PushDeer
//
//  Created by Easy on 2021/12/2.
//

import SwiftUI

struct SettingsView: View {
    @EnvironmentObject private var store: AppState
    var body: some View {
        Button("去消息页面"){
            store.tab_selected = 1
        }
    }
}

struct SettingsView_Previews: PreviewProvider {
    static var previews: some View {
        SettingsView()
    }
}
