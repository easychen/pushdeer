//
//  DeviceView.swift
//  PushDeer
//
//  Created by Easy on 2021/12/2.
//

import SwiftUI
import UIKit
import UserNotifications
import PromiseKit
// import Toast_Swift

struct DeviceView: View {
    @EnvironmentObject private var store: AppState
    @State private var showMessage: Bool = false
    
    var body: some View {
        NavigationView{
            List{
                do{
                    #if APPCLIP
                        let clip_string = " Clip"
                    #else
                        let clip_string = ""
                    #endif
                    Button("注册当前设备("+UIDevice.current.name+clip_string+")"){
                        // self.info = "clicked"
                        print("clicked")
                        print( AppDelegate.device_token )
                        // 通过接口注册设备
                        // regDevices
                        firstly{() -> Promise<[DeviceItem]> in
                            #if APPCLIP
                                let is_clip = 1
                            #else
                                let is_clip = 0
                            #endif
                            print( "is_clip" ,  is_clip )
                            return store.regDevicePromise(name: UIDevice.current.name, device_id: AppDelegate.device_token, is_clip:is_clip)
                        }.done{ (items: [DeviceItem]) in
                            print( items[0] )
                            showMessage = true
                        }
                        .ensure{
                            print( "done" )
                        }
                }.alert("设备注册完成", isPresented: $showMessage) {
                    Button("OK", role: .cancel) { }}
                }
                
                if( !store.devices.isEmpty ){
                    ForEach( store.devices  ){ device in
                        let clip_text = device.is_clip == 1 ? " Clip" : " "
                        
                        if( device.device_id == AppDelegate.device_token ){
                            Text(device.name+clip_text+"(已注册)")
                        }else{
                            Text(device.name+clip_text)
                        }
                        
                        
                    }.onDelete { (indexSet) in
                        // store.devices.remove(atOffsets: indexSet)
                        // 删除
                        indexSet.forEach{
                            (i) in
                            firstly{() -> Promise<String> in
                                return store.rmDevicePromise(did: store.devices[i].id)
                            }.done{ (message:String) in
                                store.devices.remove(atOffsets: indexSet)
                                store.loadDevices()
                                // showMessage = true
                            }
                            .ensure{
                                print( "done" )
                            }
                        }
                        
                    }
                }
                
                
            }
            .navigationBarTitle("设备",displayMode: .inline)
            .onAppear(perform: {
                store.loadDevices()
            })
        }
        
    }
}

struct DeviceView_Previews: PreviewProvider {
    static var previews: some View {
        DeviceView().environmentObject(AppState())
    }
}
