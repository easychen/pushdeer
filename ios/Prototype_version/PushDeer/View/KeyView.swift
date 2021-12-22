//
//  KeyView.swift
//  PushDeer
//
//  Created by Easy on 2021/12/2.
//

import SwiftUI
import PromiseKit

struct KeyView: View {
    @EnvironmentObject private var store: AppState
    @State private var showMessage: Bool = false
    
    var body: some View {
        NavigationView{
            List{
                if( !store.keys.isEmpty ){
                    ForEach( store.keys  ){ key in
                        Button(key.key){
                            UIPasteboard.general.string = key.key
                            showMessage=true
                        }.alert("Key已复制到剪贴板", isPresented: $showMessage){
                            Button("OK", role: .cancel) { }}
                        .swipeActions(edge: .leading){
                            Button("重新生成"){
                                firstly{() -> Promise<String> in
                                    return store.regenKeyPromise(kid: key.id)
                                }.done{ (message:String) in
                                    store.loadKeys()
                                    // showMessage = true
                                }
                                .ensure{
                                    print( "done" )
                                }
                                
                            }.tint(.orange)
                        }
                        
                        
                    }
                }else{
                    Button("生成push key"){
                        firstly{() -> Promise<[KeyItem]> in
                            return store.genKeyPromise()
                        }.done{ (items: [KeyItem]) in
                            print( items[0] )
                            // showMessage = true
                        }
                        .ensure{
                            print( "done" )
                        }
                    }
                }
                
                
            }.navigationBarTitle("Key",displayMode: .inline)
            .onAppear(perform: {
                store.loadKeys()
                print("key")
            })
        }
    }
}

struct KeyView_Previews: PreviewProvider {
    static var previews: some View {
        KeyView()
    }
}
