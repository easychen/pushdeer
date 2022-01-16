# PushDeer-iOS

PushDeer 是一个开放源码的无 App 推送服务.

这里是 PushDeer 的 iOS 端的源码, 支持 iOS / iPadOS / macOS.


PushDeer-iOS 最低支持 iOS14, 因为苹果轻应用(App Clips)最低支持 iOS14.

由于苹果规定轻应用必须随 APP 一起发布, 所以除了扫码打开轻应用即时享用推送服务, 实际上你也可以下载 APP 使用.


## 开发相关

- 语言使用 Swift
- 界面使用 SwiftUI
- 异步使用 Swift Concurrency ( async / await )
- 第三方库管理 CocoaPods / Swift Package Manager

## 构建环境

- Xcode 13.2.1  
( Swift Concurrency 本来只支持 iOS 15, 从 Xcode 13.2 开始向下兼容到 iOS 13 )
- CocoaPods 1.11.2

## 运行项目

1. 运行项目前请先安装 CocoaPods, 并在项目根目录下执行: `pod install`

2. 然后使用 Xcode 打开项目, 等待 Swift Package Manager 工具自动自动安装依赖库, 完成后即可运行项目.

3. 如遇 CocoaPods 和 Swift Package Manager 管理的第三方库被墙, 请自行搜索解决.

### 依赖清单

ps: 只列出一级依赖库, 不包含依赖的依赖, 但同样感谢

CocoaPods

- Moya
- SDWebImageSwiftUI
- KRProgressHUD

Swift Package Manager

- MarkdownUI

