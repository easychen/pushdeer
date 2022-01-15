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
          .foregroundColor(Color(UIColor.darkGray))
        Text(messageItem.createdDateStr)
          .font(.system(size: 12))
          .foregroundColor(Color(UIColor.darkGray))
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

struct MessageContentView: View {
  let messageItem: MessageModel
  @State private var image: PlatformImage? = nil
  
  var body: some View {
    switch messageItem.type {
    case "markdown":
      CardView {
        VStack(alignment: .leading, spacing: 5) {
          Markdown(Document(messageItem.text ?? ""))
            .markdownStyle(
              DefaultMarkdownStyle(
                font: .system(size: 14),
                foregroundColor: UIColor.darkGray
              )
            )
          if !(messageItem.desp?.isEmpty ?? true) {
            Markdown(Document(messageItem.desp!))
              .markdownStyle(
                DefaultMarkdownStyle(
                  font: .system(size: 14),
                  foregroundColor: UIColor.darkGray
                )
              )
          }
        }
        .padding()
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
            Color.gray.opacity(0.5)
            Image(systemName: "photo")
              .foregroundColor(.gray)
              .font(.system(size: 100))
          }
          .frame(width: nil, height: 200, alignment: .center)
        })
        .indicator(.activity)
        .transition(.fade(duration: 0.5))
        .scaledToFill()
        .background(Color.white)
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
              .foregroundColor(Color(UIColor.darkGray))
            Spacer(minLength: 0)
          }
          if !(messageItem.desp?.isEmpty ?? true) {
            Text(messageItem.desp ?? "")
              .font(.system(size: 14))
              .foregroundColor(Color(UIColor.darkGray))
          }
        }
        .padding()
        .contextMenu {
          Button {
            UIPasteboard.general.string = (messageItem.text ?? "") + (messageItem.desp ?? "")
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
