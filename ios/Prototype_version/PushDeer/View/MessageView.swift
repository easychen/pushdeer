//
//  MessageView.swift
//  PushDeer
//
//  Created by Easy on 2021/12/2.
//

import SwiftUI
// import MarkdownUI

struct MessageView: View {
    @EnvironmentObject private var store: AppState
    @State private var showMessage: Bool = false
    
    var body: some View {
        NavigationView{
            List{
                ForEach( store.messages  ){ message in
                    VStack(alignment: .leading){
                        Text(message.text).font(.title3)
//                        if(message.type == "markdown"){
//                            let doc = Document(message.desp)
//                            Markdown(doc)
//                        }else
                        do
                        {
                            //Text(message.desp)
                            Button(message.desp){
                                UIPasteboard.general.string = message.text+"\r\n"+message.desp
                                showMessage=true
                            }.alert("消息已复制到剪贴板", isPresented: $showMessage){
                                Button("OK", role: .cancel) { }}
                        }
                        
                    }
                    // .padding()
                }.swipeActions(edge: .leading){
//                    Button("重新生成"){
//                        //
//                    }.tint(.orange)
                }
            }.navigationBarTitle("消息",displayMode: .inline)
                .onAppear(perform: {
                    
                    store.loadMessages()
//                    var timer = Timer.scheduledTimer(withTimeInterval: 30, repeats: true) {
//                        (_) in
//                        store.loadMessages()
//                    }
            })
        }.refreshable {
            store.loadMessages()
       }
    }
}

struct MessageView_Previews: PreviewProvider {
    static var previews: some View {
        MessageView()
    }
}
