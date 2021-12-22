//
//  AppState.swift
//  PushDeer
//
//  Created by Easy on 2021/12/1.
//

import Foundation
import Moya
import PromiseKit

class AppState: ObservableObject {
    @Published var token : String? = nil
    @Published var devices: [DeviceItem] = []
    @Published var keys: [KeyItem] = []
    @Published var messages: [MessageItem] = []
    @Published var tab_selected: Int = 1
    @Published var device_token: String = ""
    
    // @Published var messagesFetched: [Message] = []
    
    func login(token: String){
        let p = MoyaProvider<PushDeerApi>()
        p.request(.login(idToken: token)){ result in
            //
            switch result {
                case let .success(moyaResponse):
                    do{
                        let result = try JSONDecoder().decode(TokenResult.self, from: moyaResponse.data)
                        print( result )
                        DispatchQueue.main.async{
                            self.token = result.content.token
                        }
                    }catch{
                        print(error)
                    }

                case let .failure(error):
                    print( error )
            }
        }
        print("out")
    }
    
    func loginPromise( token:String )-> Promise<TokenResult>
    {
        let p = MoyaProvider<PushDeerApi>()
        return Promise<TokenResult>{resolver in
            p.request(.login(idToken: token)){ result in
                //
                switch result {
                    case let .success(moyaResponse):
                        do{
                            let result = try JSONDecoder().decode(TokenResult.self, from: moyaResponse.data)
                            print( result )
                            resolver.fulfill( result )
                        }catch{
                            resolver.reject(error)
                        }

                        case let .failure(error):
                            resolver.reject(error)
                }
            }
        }
        
    }
    
    func regDevicePromise(name: String, device_id: String, is_clip: Int)->Promise<[DeviceItem]>{
        let p = MoyaProvider<PushDeerApi>()
        return Promise<[DeviceItem]>{resolver in
            p.request(.regDevice(token: self.token!, name: name, device_id: device_id, is_clip: is_clip)){ result in
                switch result {
                    case let .success(moyaResponse):
                    print( String(data:moyaResponse.data, encoding: .utf8) )
                    do{
                        let result = try JSONDecoder().decode( DeviceResult.self, from: moyaResponse.data )
                        DispatchQueue.main.async{
                            self.devices = result.content.devices
                        }
                        resolver.fulfill(result.content.devices)
                    }catch{
                        resolver.reject(error)
                    }
                    
                    case let .failure(error):
                        resolver.reject(error)
                }
            }
            
        }
    }
    
    func loadDevicesPromise()-> Promise<[DeviceItem]>{
        let p = MoyaProvider<PushDeerApi>()
        return Promise<[DeviceItem]>{resolver in
            p.request(.getDevices(token:self.token!)){ result in
                switch result {
                    case let .success(moyaResponse):
                    // print( String(data:moyaResponse.data, encoding: .utf8) )
                    do{
                        // DecoderDates().decode(Codable.self, from: data)
                        let result = try JSONDecoder().decode( DeviceResult.self, from: moyaResponse.data )
                        DispatchQueue.main.async{
                            self.devices = result.content.devices
                        }
                        resolver.fulfill(result.content.devices)
                    }catch{
                        resolver.reject(error)
                    }
                    
                    case let .failure(error):
                        resolver.reject(error)
                }
            }
            
        }
    }
    
    
    
    func loadDevices()  {
        var devices : [DeviceItem] = []
        let p = MoyaProvider<PushDeerApi>()
        if( self.token != nil ){
            p.request(.getDevices(token:self.token!)){ result in
                switch result {
                    case let .success(moyaResponse):
                    // print( String(data:moyaResponse.data, encoding: .utf8) )
                    do{
                        let result = try JSONDecoder().decode( DeviceResult.self, from: moyaResponse.data )
                        DispatchQueue.main.async{
                            self.devices = result.content.devices
                        }
                        devices = result.content.devices
                    }catch{
                        print(error)
                    }
                    
                    case let .failure(error):
                        print( error )
                }
            }
        }
        
        print( devices )
        
    }
    
    func loadKeys()  {
        var keys : [KeyItem] = []
        let p = MoyaProvider<PushDeerApi>()
        if( self.token != nil ){
            p.request(.getKeys(token:self.token!)){ result in
                switch result {
                    case let .success(moyaResponse):
                    // print( String(data:moyaResponse.data, encoding: .utf8) )
                    do{
                        let result = try JSONDecoder().decode( KeyResult.self, from: moyaResponse.data )
                        DispatchQueue.main.async{
                            self.keys = result.content.keys
                        }
                        keys = result.content.keys
                    }catch{
                        print(error)
                    }

                    case let .failure(error):
                        print( error )
                }
            }
        }

        print( keys )

    }
    
    func genKeyPromise()->Promise<[KeyItem]>{
        let p = MoyaProvider<PushDeerApi>()
        return Promise<[KeyItem]>{resolver in
            p.request(.genKey(token: self.token!)){ result in
                switch result {
                    case let .success(moyaResponse):
                    print( String(data:moyaResponse.data, encoding: .utf8) )
                    do{
                        let result = try JSONDecoder().decode( KeyResult.self, from: moyaResponse.data )
                        DispatchQueue.main.async{
                            self.keys = result.content.keys
                        }
                        resolver.fulfill(result.content.keys)
                    }catch{
                        resolver.reject(error)
                    }
                    
                    case let .failure(error):
                        resolver.reject(error)
                }
            }
            
        }
    }
    
    func loadMessages()  {
        var messages : [MessageItem] = []
        let p = MoyaProvider<PushDeerApi>()
        if( self.token != nil ){
            p.request(.getMessages(token:self.token!)){ result in
                switch result {
                    case let .success(moyaResponse):
                    // print( String(data:moyaResponse.data, encoding: .utf8) )
                    do{
                        let result = try JSONDecoder().decode( MessageResult.self, from: moyaResponse.data )
                        DispatchQueue.main.async{
                            self.messages = result.content.messages
                        }
                        messages = result.content.messages
                    }catch{
                        print(error)
                    }

                    case let .failure(error):
                        print( error )
                }
            }
        }

        print( messages )

    }
    
    func rmDevicePromise(did: Int)->Promise<String>{
        let p = MoyaProvider<PushDeerApi>()
        return Promise<String>{resolver in
            p.request(.rmDevice(token: self.token!, did: did)){ result in
                switch result {
                    case let .success(moyaResponse):
                    print( String(data:moyaResponse.data, encoding: .utf8) )
                    do{
                        let result = try JSONDecoder().decode( ActionResult.self, from: moyaResponse.data )
//                        DispatchQueue.main.async{
//                            self.devices = result.content.devices
//                        }
                        resolver.fulfill(result.content.message)
                    }catch{
                        resolver.reject(error)
                    }
                    
                    case let .failure(error):
                        resolver.reject(error)
                }
            }
            
        }
    }
    
    func regenKeyPromise(kid: Int)->Promise<String>{
        let p = MoyaProvider<PushDeerApi>()
        return Promise<String>{resolver in
            p.request(.regenKey(token: self.token!, kid: kid)){ result in
                switch result {
                    case let .success(moyaResponse):
                    print( String(data:moyaResponse.data, encoding: .utf8) )
                    do{
                        let result = try JSONDecoder().decode( ActionResult.self, from: moyaResponse.data )
//                        DispatchQueue.main.async{
//                            self.devices = result.content.devices
//                        }
                        resolver.fulfill(result.content.message)
                    }catch{
                        resolver.reject(error)
                    }
                    
                    case let .failure(error):
                        resolver.reject(error)
                }
            }
            
        }
    }
    

    
}
