//
//  LoginView.swift
//  PushDeer
//
//  Created by Easy on 2021/12/1.
//

import SwiftUI
import AuthenticationServices

struct LoginView: View {
    @EnvironmentObject private var store: AppState
    
    var body: some View {
        VStack{
            Spacer()
            Image("logo")
            #if APPCLIP
                Text("Clip version")
            #endif
//            Text("PushDeer𐂂").font(.title).padding()
//            Button("login"){
//                store.token = "edbbf536d6818d49579d988bdf119df8"
//            }
            Spacer()
            SignInWithAppleButton(
                    onRequest: { request in
                        request.requestedScopes = [.fullName, .email]
                    },
                    onCompletion: { result in
                        switch result {
                            case .success(let authResults):
                                switch authResults.credential {
                                    case let appleIDCredential as ASAuthorizationAppleIDCredential:
                                    // 000791.7a323f1326dd4674bc16d32fd6339875.1424 Optional(givenName: lijie familyName: chen ) Optional("easychen@qq.com")
                                    print(appleIDCredential.user,appleIDCredential.fullName,appleIDCredential.email)
                                    print(String(data:appleIDCredential.identityToken! , encoding: .utf8 ))
                                    
                                    // store.token = String(data:appleIDCredential.identityToken! , encoding: .utf8 )
                                    let idToken = String(data:appleIDCredential.identityToken! , encoding: .utf8 )
                                    
                                    store.login(token: idToken!)
                                    
                                    
                                    
                                    case let passwordCredential as ASPasswordCredential:
                                        let username = passwordCredential.user
                                        let password = passwordCredential.password
                                        print(username, password)
                                    default:
                                        break
                                }
                            case .failure(let error):
                                print("failure", error)
                        }
                    }
                ).frame( width: 300 , height: 64 )

            Spacer()
        }
    }
}

struct LoginView_Previews: PreviewProvider {
    static var previews: some View {
        LoginView()
    }
}
