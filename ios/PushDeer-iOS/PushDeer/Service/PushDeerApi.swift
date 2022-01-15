//
//  PushDeerApi.swift
//  PushDeer
//
//  Created by HEXT on 2022/1/4.
//

import Foundation
import Moya

enum PushDeerApi {
  
  case fake
  case login(idToken: String)
  case getUserInfo(token: String)
  
  case regDevice(token: String, name: String, device_id: String, is_clip: Int)
  case renameDevice(token: String, id: Int, name: String)
  case getDevices(token: String)
  case rmDevice(token: String, id: Int)
  
  case genKey(token: String)
  case regenKey(token: String, id: Int)
  case renameKey(token: String, id: Int, name: String)
  case getKeys(token: String)
  case rmKey(token: String, id: Int)
  
  /// type: 文本=text，markdown，图片=image，默认为markdown
  case push(pushkey: String, text: String, desp: String, type: String)
  
  case getMessages(token: String, limit: Int)
  case rmMessage(token: String, id: Int)
  
}

extension PushDeerApi: TargetType {
  var baseURL: URL { URL( string: "http://pushdeer.wskfz.com:8800" )! }
  var path: String {
    switch self {
    case .fake:
      return "/login/fake"
    case .login:
      return "/login/idtoken"
    case .getUserInfo:
      return "/user/info"
      
    case .regDevice:
      return "/device/reg"
    case .renameDevice:
      return "/device/rename"
    case .getDevices:
      return "/device/list"
    case .rmDevice:
      return "/device/remove"
      
    case .genKey:
      return "/key/gen"
    case .regenKey:
      return "/key/regen"
    case .renameKey:
      return "/key/rename"
    case .getKeys:
      return "/key/list"
    case .rmKey:
      return "/key/remove"
      
    case .push:
      return "/message/push"
      
    case .getMessages:
      return "/message/list"
    case .rmMessage:
      return "/message/remove"
    }
  }
  var method: Moya.Method {
    switch self {
    default:
      return .post
    }
  }
  var task: Task {
    switch self {
    case .fake:
      return .requestParameters(parameters: [:], encoding: URLEncoding.queryString)
    case let .login(idToken):
      return .requestParameters(parameters: ["idToken": idToken], encoding: URLEncoding.queryString)
    case let .getUserInfo(token):
      return .requestParameters(parameters: ["token": token], encoding: URLEncoding.queryString)
      
    case let .regDevice(token, name, device_id, is_clip):
      return .requestParameters(parameters: ["token": token,"name": name, "device_id": device_id,"is_clip": is_clip], encoding: URLEncoding.queryString)
    case let .renameDevice(token, id, name):
      return .requestParameters(parameters: ["token": token,"id": id,"name": name], encoding: URLEncoding.queryString)
    case let .getDevices(token):
      return .requestParameters(parameters: ["token": token], encoding: URLEncoding.queryString)
    case let .rmDevice(token, id):
      return .requestParameters(parameters: ["token": token,"id": id], encoding: URLEncoding.queryString)
      
    case let .genKey(token):
      return .requestParameters(parameters: ["token": token], encoding: URLEncoding.queryString)
    case let .regenKey(token, id):
      return .requestParameters(parameters: ["token": token,"id": id], encoding: URLEncoding.queryString)
    case let .renameKey(token, id, name):
      return .requestParameters(parameters: ["token": token,"id": id,"name": name], encoding: URLEncoding.queryString)
    case let .getKeys(token):
      return .requestParameters(parameters: ["token": token],encoding: URLEncoding.queryString)
    case let .rmKey(token, id):
      return .requestParameters(parameters: ["token": token, "id": id],encoding: URLEncoding.queryString)
      
    case let .push(pushkey, text, desp, type):
      return .requestParameters(parameters: ["pushkey": pushkey, "text": text, "desp": desp, "type": type],encoding: URLEncoding.queryString)
      
    case let .getMessages(token, limit):
      return .requestParameters(parameters: ["token": token, "limit": limit],encoding: URLEncoding.queryString)
    case let .rmMessage(token, id):
      return .requestParameters(parameters: ["token": token, "id": id],encoding: URLEncoding.queryString)
      
    }
  }
  var headers: [String: String]? {
    return ["Content-type": "application/json"]
  }
}
