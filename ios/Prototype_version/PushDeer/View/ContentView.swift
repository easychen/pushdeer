//
//  ContentView.swift
//  PushDeer
//
//  Created by Easy on 2021/11/30.
//

import SwiftUI
import PromiseKit

struct ContentView: View {
    @EnvironmentObject private var store: AppState
    @State private var show_view = "init"
    
    var body: some View {
        if(( store.token ) != nil){
//            if( show_view == "init" ){
//                LoginedView()
//            }else{
//                LoginedView()
//            }
            
            switch show_view {
            case "init":
                LoginedView().onAppear(perform: {

                    firstly{() -> Promise<[DeviceItem]> in
                        return store.loadDevicesPromise()
                    }.done{ (items: [DeviceItem]) in
                        print("loaded"+store.device_token)
                        // UIApplication.shared.delegate.deviceToken
                        dump( items )
                    }
                    .ensure{
                        // 开始判断
                        if( store.devices.isEmpty ){
                            // navigate to device view
                            show_view = "device"
                        }else{
                            // navigate to message view
                            show_view = "message"
                        }
                        print("done")
                    }
                })
            case "device":
                // DeviceView()
                MainView().onAppear(perform: {store.tab_selected = 2})
            case "message":
                MainView().onAppear(perform: {store.tab_selected = 1})
            default:
                LoginedView()
            }
            
            
//            if( show_view == "init" ){
//                LoginedView().onAppear(perform: {
//
//                    firstly{() -> Promise<[DeviceItem]> in
//                        return store.loadDevicesPromise()
//                    }.done{ (items: [DeviceItem]) in
//                        print("loaded")
//                        dump( items )
//                    }
//                    .ensure{
//                        // 开始判断
//                        if( store.devices.isEmpty ){
//                            // navigate to device view
//                            show_view = "device"
//                        }else{
//                            // navigate to message view
//                            show_view = "message"
//                        }
//                        print("done")
//                    }
//                })
//            }
            
            
//            switch( $show_view ){
//                case "init":
//                    LoginedView().onAppear(perform: {
//
//                        firstly{() -> Promise<[DeviceItem]> in
//                            return store.loadDevicesPromise()
//                        }.done{ (items: [DeviceItem]) in
//                            print("loaded")
//                            dump( items )
//                        }
//                        .ensure{
//                            // 开始判断
//                            if( store.devices.isEmpty ){
//                                // navigate to device view
//                                $show_view = "device"
//                            }else{
//                                // navigate to message view
//                                $show_view = "message"
//                            }
//                            print("done")
//                        }
//                    })
//                case "device":
//                    print("device")
//                case "message":
//                    print("message")
//
//            }
            // if( $show_view == "init" )
            // 登入后界面
            // 检查 device 是否为空
            // 如果为空，自动注册，并跳转到 message 界面
            //
            
        }
        else{
            // 未登入界面
            LoginView().onAppear(perform: {
                print( AppDelegate.device_token )
            })
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
