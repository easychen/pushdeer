//
//  Call.swift
//  PushDeer
//
//  Created by Easy on 2021/12/2.
//
import Moya
import PromiseKit

func CallApi<T: TargetType>(_ target: T) -> Promise<String>{
    
    let provider = MoyaProvider<T>()
    return Promise<String>{ resolver in
//        if( true ){
//            resolver.fulfill( "123" )
//        }else{
//            resolver.reject( "error" )
//        }
        provider.request(target, completion: { (result) in
            switch result {
                case let .success(moyaResponse):
                    resolver.fulfill(String(data: moyaResponse.data, encoding: .utf8)!)
//                    guard moyaResponse.statusCode == 200 else {
//                        resolver.reject(error)
//                    }
                case let .failure(error):
                    resolver.reject(error)
            }
        });
    }
//    return Promise<Codable> { fulfill, reject in
//        provider.request(target, completion: { (result) in
//            switch result {
//            case let .success(moyaResponse):
//                // let resp = JSON(data: moyaResponse.data)
//
//                guard moyaResponse.statusCode == 200 else {
//                    reject(error)
//                }
//
//                // http status code is now 200 from here on
//
//                fulfill(moyaResponse.data)
//            case let .failure(error):
//                reject(error)
//            }
//        })
//    }
    // return "123"
}
