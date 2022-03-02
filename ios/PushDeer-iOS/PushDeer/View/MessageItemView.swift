//
//  MessageItemView.swift
//  PushDeer
//
//  Created by HEXT on 2021/12/29.
//

import SwiftUI
import MarkdownUI
import SDWebImageSwiftUI
import Photos

struct MessageItemView: View {
  let messageItem: MessageModel
  /// 删除按钮点击的回调
  let deleteAction : () -> ()
  
  var body: some View {
    VStack {
      HStack {
        HLine().stroke(Color(UIColor.lightGray))
          .frame(width: 20, height: 1)
        Image("avatar2")
          .resizable()
          .scaledToFit()
          .frame(width: 38, height: 38)
        Text(messageItem.pushkey_name ?? "")
          .font(.system(size: 14))
          .foregroundColor(Color("text2Color"))
        Text(messageItem.createdDateStr)
          .font(.system(size: 12))
          .foregroundColor(Color("text2Color"))
        HLine().stroke(Color(UIColor.lightGray))
          .frame(height: 1)
      }
      
      DeletableView(contentView: {
        MessageContentView(messageItem: messageItem)
      }, deleteAction: deleteAction)
        .padding(messageItem.type == "image" ? EdgeInsets.init() : EdgeInsets(top: 10, leading: 26, bottom: 0, trailing: 24))
      
    }
    .padding(.top, 25)
  }
}

extension URL: Identifiable {
  public var id: Self { self }
}

struct MessageContentView: View {
  let messageItem: MessageModel
  @EnvironmentObject private var store: AppState
  @State private var image: PlatformImage? = nil
  @State private var showUrl: URL?
  @State private var showActionSheet = false
  
  var body: some View {
    switch messageItem.type {
    case "markdown":
      CardView {
        VStack(alignment: .leading, spacing: 5) {
          Markdown(messageItem.text ?? "")
            .markdownStyle(
              MarkdownStyle(
                font: .system(size: 14),
                foregroundColor: .init("textColor")
              )
            )
#if !targetEnvironment(macCatalyst)
            .onOpenMarkdownLink { url in
              if store.isUseBuiltInBrowser {
                showUrl = url
              } else {
                UIApplication.shared.open(url, options: [:], completionHandler: nil)
              }
            }
#endif
          if !(messageItem.desp?.isEmpty ?? true) {
            Markdown(messageItem.desp ?? "")
              .markdownStyle(
                MarkdownStyle(
                  font: .system(size: 14),
                  foregroundColor: .init("textColor")
                )
              )
#if !targetEnvironment(macCatalyst)
              .onOpenMarkdownLink { url in
                if store.isUseBuiltInBrowser {
                  showUrl = url
                } else {
                  UIApplication.shared.open(url, options: [:], completionHandler: nil)
                }
              }
#endif
          }
        }
        .padding()
//#if targetEnvironment(macCatalyst)
        .contextMenu {
          Button {
            UIPasteboard.general.string = (messageItem.text ?? "") + "\n" + (messageItem.desp ?? "")
            HToast.showSuccess(NSLocalizedString("已复制", comment: ""))
          } label: {
            Label("复制",systemImage: "doc.on.doc")
          }
        }
//#endif
//#if !targetEnvironment(macCatalyst)
//        .onLongPressGesture {
//          UIImpactFeedbackGenerator().impactOccurred()
//          showActionSheet = true
//        }
//        .actionSheet(isPresented: $showActionSheet) {
//          ActionSheet(title: Text("复制消息内容"), message: nil, buttons: [
//            .default(Text("复制"), action: {
//              UIPasteboard.general.string = (messageItem.text ?? "") + "\n" + (messageItem.desp ?? "")
//              HToast.showSuccess(NSLocalizedString("已复制", comment: ""))
//            }),
//            .cancel()
//          ])
//        }
//#endif
      }
      .fullScreenCover(item: $showUrl) {
        
      } content: { url in
        SafariView(url: url)
      }
      
    case "image":
      WebImage(url: URL(string: messageItem.text ?? ""))
        .onSuccess { image, data, cacheType in
          DispatchQueue.main.async {
            self.image = image
          }
        }
        .resizable()
        .placeholder(content: {
          ZStack {
            Color("background2Color")
            Image(systemName: "photo")
              .foregroundColor(.gray)
              .font(.system(size: 100))
          }
          .frame(width: nil, height: 200, alignment: .center)
        })
        .indicator(.activity)
        .transition(.fade(duration: 0.5))
        .scaledToFill()
        .background(Color("backgroundColor"))
        .contextMenu {
          Button {
            guard let image = image else {
              HToast.showWarning(NSLocalizedString("图片未加载成功", comment: ""))
              return
            }
            UIPasteboard.general.image = image
            HToast.showSuccess(NSLocalizedString("已拷贝", comment: ""))
          } label: {
            Label("拷贝图片",systemImage: "doc.on.doc")
          }
          Button {
            guard let image = image else {
              HToast.showWarning(NSLocalizedString("图片未加载成功", comment: ""))
              return
            }
            PHPhotoLibrary.shared().performChanges {
              PHAssetChangeRequest.creationRequestForAsset(from: image)
            } completionHandler: { (isSuccess, error) in
              DispatchQueue.main.async {
                if isSuccess {// 成功
                  print("Success")
                  HToast.showSuccess(NSLocalizedString("保存成功", comment: ""))
                } else {
                  print(error as Any)
                  HToast.showError(NSLocalizedString("保存失败", comment: ""))
                }
              }
            }
          } label: {
            Label("保存图片",systemImage: "square.and.arrow.down")
          }
        }
      
    default:
      CardView {
        VStack(alignment: .leading, spacing: 5) {
          HStack{
            Text(messageItem.text ?? "")
              .font(.system(size: 14))
              .foregroundColor(Color("textColor"))
            Spacer(minLength: 0)
          }
          if !(messageItem.desp?.isEmpty ?? true) {
            Text(messageItem.desp ?? "")
              .font(.system(size: 14))
              .foregroundColor(Color("textColor"))
          }
        }
        .padding()
        .contextMenu {
          Button {
            UIPasteboard.general.string = (messageItem.text ?? "") + "\n" + (messageItem.desp ?? "")
            HToast.showSuccess(NSLocalizedString("已复制", comment: ""))
          } label: {
            Label("复制",systemImage: "doc.on.doc")
          }
        }
      }
    }
  }
}

struct MessageItemView_Previews: PreviewProvider {
  static var previews: some View {
    VStack {
      MessageItemView(messageItem: MessageModel(id: 1, uid: "1", text: "纯文本的效果", desp: "你好呀", type: "text", pushkey_name: "Key", created_at: "2022-01-08T18:00:48.000000Z")){}
      MessageItemView(messageItem: MessageModel(id: 1, uid: "1", text: "纯文本的效果纯文本的效果纯文本的效果纯文本的效果纯文本的效果纯文本的效果纯文本的效果", desp: "", type: "text", pushkey_name: "Key", created_at: "2022-01-08T18:00:48.000000Z")){}
      MessageItemView(messageItem: MessageModel(id: 1, uid: "1", text: "https://blog.wskfz.com/usr/uploads/2018/06/2498727457.png", desp: "", type: "image", pushkey_name: "Key1", created_at: "2022-01-08T18:00:48.000000Z")){}
      MessageItemView(messageItem: MessageModel(id: 1, uid: "1", text: "https://blog.wskfz.com/usr/uploads/2018/06/2151130181.png", desp: "", type: "image", pushkey_name: "Key2", created_at: "2022-01-08T18:00:48.000000Z")){}
      MessageItemView(messageItem: MessageModel(id: 1, uid: "1", text: "https://blog.wskfz.com/usr/uploads/2018/06/1718629805.png", desp: "", type: "image", pushkey_name: "Key2", created_at: "2022-01-08T18:00:48.000000Z")){}
      MessageItemView(messageItem: MessageModel(id: 1, uid: "1", text: "*MarkDown*的**效果**", desp: "*MarkDown*的**效果**", type: "markdown", pushkey_name: "Key", created_at: "2021-12-28T13:44:48.000000Z")){}
      MessageItemView(messageItem: MessageModel(id: 1, uid: "1", text: """
It's very easy to make some words **bold** and other words *italic* with Markdown.

**Want to experiment with Markdown?** Play with the [reference CommonMark
implementation](https://spec.commonmark.org/dingus/).
""", desp: "", type: "markdown", pushkey_name: "3", created_at: "2021-12-28T13:44:48.000000Z")){}
      Spacer()
    }
  }
}
