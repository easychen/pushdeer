//
//  Api.swift
//  PushDeer
//
//  Created by Easy on 2021/12/1.
//
import Foundation
import Moya

enum PushDeerApi{
    case login(idToken: String)
    case getDevices(token: String)
    case regDevice(token: String, name:String, device_id:String, is_clip: Int)
    case genKey(token: String)
    case getKeys(token: String)
    case getMessages(token: String)
    case regenKey(token: String, kid: Int)
    case rmDevice(token: String, did: Int)
    
    // case updateUser(firstName: String, lastName: String)
}

extension PushDeerApi: TargetType{
    var baseURL: URL { URL( string: "http://127.0.0.1:8800" )! }
    var path: String{
        switch self{
            case .login:
                return "/login/idtoken"
            case .getDevices:
                return "/user/devices"
            case .regDevice:
                return "/device/reg"
            case .genKey:
                return "/key/gen"
            case .getKeys:
                return "/user/keys"
            case .getMessages:
                return "/user/messages"
            case .regenKey:
                return "/key/regen"
            case .rmDevice:
                return "/device/rm"
        }
    }
    var method: Moya.Method{
        switch self{
        default:
            return .post
        }
    }
    var task: Task{
        switch self{
        case let .login(idToken):
            return .requestParameters(parameters: ["idToken": idToken], encoding: URLEncoding.queryString)
        case let .getDevices(token):
            return .requestParameters(parameters: ["token": token], encoding: URLEncoding.queryString)
        case let .regDevice(token,name,device_id,is_clip):
            return .requestParameters(parameters: ["token": token,"name": name, "device_id": device_id,"is_clip":is_clip], encoding: URLEncoding.queryString)
        case let .genKey(token):
            return .requestParameters(parameters: ["token": token], encoding: URLEncoding.queryString)
        case let .getKeys(token):
            return .requestParameters(parameters: ["token": token],encoding: URLEncoding.queryString)
        case let .getMessages(token):
            return .requestParameters(parameters: ["token": token],encoding: URLEncoding.queryString)
        case let .rmDevice(token,did):
            return .requestParameters(parameters: ["token": token,"did": did], encoding: URLEncoding.queryString)
        case let .regenKey(token,kid):
            return .requestParameters(parameters: ["token": token,"kid": kid], encoding: URLEncoding.queryString)
        
        // case let .updateUser(firstName, lastName):  // Always sends parameters in URL, regardless of which HTTP method is used
        // return .requestParameters(parameters: ["first_name": firstName, "last_name": lastName], encoding: URLEncoding.queryString)
        }
    }
    var headers: [String: String]? {
            return ["Content-type": "application/json"]
    }
}

