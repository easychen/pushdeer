//
//  MainView.swift
//  PushDeer
//
//  Created by Easy on 2021/12/2.
//

import SwiftUI

struct MainView: View {
    @EnvironmentObject private var store: AppState
    
    
    
    var body: some View {
        
        
        TabView(selection: $store.tab_selected){
            MessageView().tabItem({Label("消息",systemImage: "message")}).onTapGesture {
                store.tab_selected = 1
            }.tag(1)
            DeviceView().tabItem({Label("设备",systemImage: "ipad.and.iphone")}).onTapGesture {
                store.tab_selected = 2
            }.tag(2)
            KeyView().tabItem({Label("Key",systemImage: "key")}).onTapGesture {
                store.tab_selected = 3
            }.tag(3)
//            ChannelView().tabItem({Label("频道",systemImage: "waveform")}).onTapGesture {
//                store.tab_selected = 4
//            }.tag(4)
//            SettingsView().tabItem({Label("设置",systemImage: "gearshape")}).onTapGesture {
//                store.tab_selected = 5
//            }.tag(5)
            
            
        }
//        .onAppear(perform: {
//            if( !apptoken.isEmpty ){
//                store.token = UserDefaults.standard.string( forKey: "apptoken" )
//            }
//                
//           
//        })
    }
}

struct TabView_Previews: PreviewProvider {
    static var previews: some View {
        MainView().environmentObject(AppState())
    }
}
