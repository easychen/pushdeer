//
//  CommonUtils.swift
//  PushDeer
//
//  Created by HEXT on 2022/9/11.
//

import Foundation

/// 判断一个集合 **为空**
/// - Parameter emptiable: 一个可能为空的集合, 需要实现了 Collection 协议, 如: String / Array / Dictionary / Set / Data 等
/// - Returns: nil 或 空 为 true
func isEmpty<T: Collection>(_ emptiable: T?) -> Bool {
    if emptiable == nil || emptiable!.isEmpty {
        return true
    } else {
        return false
    }
}

/// 判断一个集合 **不为空**
/// - Parameter emptiable: 一个可能为空的集合, 需要实现了 Collection 协议, 如: String / Array / Dictionary / Set / Data 等
/// - Returns: nil 或 空 为 false
func isNotEmpty<T: Collection>(_ emptiable: T?) -> Bool {
    return !isEmpty(emptiable)
}
