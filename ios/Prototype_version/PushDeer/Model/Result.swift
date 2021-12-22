//
//  Result.swift
//  PushDeer
//
//  Created by Easy on 2021/12/1.
//

import Foundation



struct TokenResult: Codable{
    let code: Int
    let content: TokenContent
}

struct TokenContent: Codable{
    let token: String
}

struct DeviceItem: Codable, Identifiable{
    let id: Int
    let uid: String
    let name: String
    let type: String
    let device_id: String
    let is_clip: Int
//    let created_at: Date
//    let updated_at: Date
}

struct DeviceResult: Codable{
    let code: Int
    let content: DeviceContent
}

struct DeviceContent: Codable{
    let devices: [DeviceItem]
}

struct KeyResult: Codable{
    let code: Int
    let content: KeyContent
}

struct KeyContent: Codable{
    let keys: [KeyItem]
}

struct KeyItem: Codable, Identifiable{
    let id: Int
    let key: String
}

struct MessageResult: Codable{
    let code: Int
    let content: MessageContent
}

struct MessageContent: Codable{
    let messages: [MessageItem]
}

struct MessageItem: Codable, Identifiable{
    let id: Int
    let uid: String
    let text: String
    let desp: String
    let type: String
    // let readkey: String
}

struct ActionResult: Codable{
    let code: Int
    let content: ActionContent
}

struct ActionContent: Codable{
    let message: String
}




//class DecoderDates: JSONDecoder {
//
//    override func decode<T>(_ type: T.Type, from data: Data) throws -> T where T : Decodable {
//        let decoder = JSONDecoder()
//        let dateFormatter = DateFormatter()
//        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS"
//        decoder.dateDecodingStrategy = .formatted(dateFormatter)
//        return try decoder.decode(T.self, from: data)
//    }
//
//}


