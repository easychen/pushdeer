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
  /// 通过苹果 idToken 登入
  case login(idToken: String)
  /// 通过微信 oauth code 登入
  case wechatLogin(code: String)
  /// 合并用户并将旧用户删除
  /// | 参数 | 说明 |
  /// | - | - |
  /// | token | 认证token |
  /// | type | 字符串，必须为 apple 或者 wechat |
  /// | tokenorcode | type 为 apple时此字段为 idToken，否则为 微信code |
  case mergeUser(token: String, type: String, tokenorcode: String)
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
  case rmAllMessage(token: String)
  
}

extension PushDeerApi: TargetType {
  var baseURL: URL {
    var urlStr = Env.onlineApiEndpoint
    if Env.isSelfHosted && !AppState.shared.api_endpoint.isEmpty {
      urlStr = AppState.shared.api_endpoint
    }
    return URL(string: urlStr)!
  }
  var path: String {
    switch self {
    case .fake:
      return "/login/fake"
    case .login:
      return "/login/idtoken"
    case .wechatLogin:
      return "/login/wecode"
    case .mergeUser:
      return "/user/merge"
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
    case .rmAllMessage:
      return "/message/clean"
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
    case let .wechatLogin(code):
      return .requestParameters(parameters: ["code": code], encoding: URLEncoding.queryString)
    case let .mergeUser(token, type, tokenorcode):
      return .requestParameters(parameters: ["token": token, "type": type, "tokenorcode": tokenorcode], encoding: URLEncoding.queryString)
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
    case let .rmAllMessage(token):
      return .requestParameters(parameters: ["token": token],encoding: URLEncoding.queryString)
      
    }
  }
  var headers: [String: String]? {
    return ["Content-type": "application/json"]
  }
}
