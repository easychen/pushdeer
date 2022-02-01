//
//  EndpointView.swift
//  PushDeer
//
//  Created by Easy on 2022/2/1.
//

import SwiftUI

struct EndpointView: View {
  @EnvironmentObject private var store: AppState
  @State private var endpoint = ""
  var body: some View {
    VStack{
      Spacer()
      
      Image("logo2").resizable()
        .scaledToFit()
        .frame(height: 200)
      
      Spacer()
      
      TextField(
          "请输入API服务的 endpoint url",
          text:$endpoint
        ).frame(width: 400, height: 48)
      .padding(EdgeInsets(top: 0, leading: 6, bottom: 0, trailing: 6))
      .cornerRadius(5)
      .overlay(
          RoundedRectangle(cornerRadius: 5)
              .stroke(lineWidth: 1.0)
      )
      
      Text("您在此应用中的数据都将发送到 endpoint 指向的服务器")
      
      Button("保存") {
        //
        if endpoint.isEmpty {
          HToast.showError(NSLocalizedString("Endpoint 不能为空", comment: ""))
          return
        }
        
        if endpoint.last == "/" {
          endpoint = String(endpoint.dropLast())
        }
        
        if endpoint == "https://api2.pushdeer.com" {
          HToast.showError(NSLocalizedString("请输入自架服务器的 Endpoint 或者使用非自架版PushDeer", comment: ""))
          return
        }
        
        store.api_endpoint = endpoint
      }
      .font(.system(size: 20))
      .frame(width: 104, height: 42)
      .foregroundColor(Color.white)
      .background(Color("BtnBgColor"))
      .cornerRadius(8)
      .padding(EdgeInsets(top: 12, leading: 26, bottom: 0, trailing: 24))
      
      Spacer()
      }
    }
    
    
}

struct EndpointView_Previews: PreviewProvider {
    static var previews: some View {
        EndpointView().environmentObject(AppState.shared)
    }
}
