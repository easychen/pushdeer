//
//  PushDeerWidget.swift
//  PushDeerWidget
//
//  Created by HEXT on 2022/3/2.
//

import WidgetKit
import SwiftUI
import Intents

struct Provider: IntentTimelineProvider {
  
  func placeholder(in context: Context) -> SimpleEntry {
    SimpleEntry(date: Date(), configuration: ConfigurationIntent())
  }
  
  func getSnapshot(for configuration: ConfigurationIntent, in context: Context, completion: @escaping (SimpleEntry) -> ()) {
    let entry = SimpleEntry(date: Date(), configuration: configuration)
    completion(entry)
  }
  
  func getTimeline(for configuration: ConfigurationIntent, in context: Context, completion: @escaping (Timeline<Entry>) -> ()) {
    NSLog("getTimeline")
    Task {
      let currentDate = Date()
      var entries: [SimpleEntry] = []
      var entry = SimpleEntry(date: currentDate, configuration: configuration)
      AppState.shared.reloadUserDefaults()
      print("token", AppState.shared.token)
      do {
        let messages = try await HttpRequest.getMessages().messages
        entry.messages = handleList(messages, context: context)
        NSLog("getMessages-success")
      } catch {
        NSLog("getMessages-catch")
      }
      entries.append(entry)
      
      let nextDate = Calendar.current.date(byAdding: .minute, value: 10, to: currentDate)!
      let timeline = Timeline(entries: entries, policy: .after(nextDate))
      completion(timeline)
    }
  }
  
  func handleList(_ origList: [MessageItem], context: Context) -> [MessageItem] {
    var list = origList
    var limit = 0
    switch context.family {
    case .systemSmall, .systemMedium:
      limit = 4
    default:
      limit = 10
    }
    if list.count <= limit + 1 {
      return list;
    }
    list = list.prefix(limit) + []
    list.append(
      MessageItem(id: -1, uid: "", text: "+其它\(origList.count - limit)条", desp: "", type: "text", pushkey_name: "", created_at: "")
    )
    return list;
  }
}

struct SimpleEntry: TimelineEntry {
  let date: Date
  let configuration: ConfigurationIntent
  var messages: [MessageItem] = placeholderList
}

struct PushDeerWidgetEntryView : View {
  var entry: Provider.Entry
  
  var body: some View {
    HContentView {
      if AppState.shared.token.isEmpty {
        Text("未登录")
      } else if entry.messages.isEmpty {
        Text("无消息")
      } else {
        VStack(alignment: .leading, spacing: 5) {
          ForEach(entry.messages) {
            if entry.messages.first?.id != $0.id {
              Divider()
            }
            if $0.id == -1 {
              HText(text: $0.text)
                .foregroundColor(.accentColor)
            } else {
              HText(text: $0.text)
            }
          }
          .font(.system(size: 15))
          Spacer(minLength: 0)
        }
        .padding(.top, 5)
        .padding()
        .accentColor(Color("AccentColor"))
      }
    }
  }
}

struct HContentView<Content : View>: View {
  /// 页面主体View
  @ViewBuilder let contentView: Content
  
  @Environment(\.colorScheme) private var colorScheme
  
  var body: some View {
    ZStack {
      // VStack HStack Spacer 组合起来撑到最大
      VStack {
        HStack {
          Spacer()
        }
        Spacer()
      }
      contentView
    }
    .background(
      Image("deer.gray")
        .opacity(colorScheme == .dark ? 0.4 : 1),
      alignment: .topTrailing
    )
  }
}

struct HText: View {
  let text: String
  var body: some View {
    if #available(iOSApplicationExtension 15.0, *) {
      Group {
        if #available(iOSApplicationExtension 15.0, *) {
          Text(try! AttributedString(markdown: text))
        }
      }
    } else {
      Text(verbatim: text)
    }
  }
}

@main
struct PushDeerWidget: Widget {
  let kind: String = "PushDeerWidget"
  
  var body: some WidgetConfiguration {
    IntentConfiguration(kind: kind, intent: ConfigurationIntent.self, provider: Provider()) { entry in
      PushDeerWidgetEntryView(entry: entry)
    }
    .configurationDisplayName("最近的PushDeer消息")
    .description("这个小部件可以快捷展示你最近收到的消息.")
  }
}

struct PushDeerWidget_Previews: PreviewProvider {
  static var previews: some View {
    AppState.shared.token = "1"
    return Group {
      PushDeerWidgetEntryView(entry: SimpleEntry(date: Date(), configuration: ConfigurationIntent()))
        .previewContext(WidgetPreviewContext(family: .systemSmall))
      PushDeerWidgetEntryView(entry: SimpleEntry(date: Date(), configuration: ConfigurationIntent()))
        .previewContext(WidgetPreviewContext(family: .systemMedium))
      PushDeerWidgetEntryView(entry: SimpleEntry(date: Date(), configuration: ConfigurationIntent()))
        .previewContext(WidgetPreviewContext(family: .systemLarge))
      if #available(iOSApplicationExtension 15.0, *) {
        PushDeerWidgetEntryView(entry: SimpleEntry(date: Date(), configuration: ConfigurationIntent()))
          .previewContext(WidgetPreviewContext(family: .systemExtraLarge))
      }
    }
  }
}

let placeholderList = [
  MessageItem(id: 1, uid: "", text: "展示最新通知", desp: "", type: "text", pushkey_name: "", created_at: ""),
  MessageItem(id: 2, uid: "", text: "方便快速查看", desp: "", type: "text", pushkey_name: "", created_at: ""),
  MessageItem(id: 3, uid: "", text: "保持消息同步", desp: "", type: "text", pushkey_name: "", created_at: ""),
  MessageItem(id: 4, uid: "", text: "自动即时刷新", desp: "", type: "text", pushkey_name: "", created_at: ""),
  MessageItem(id: -1, uid: "", text: "+其它\(8)条", desp: "", type: "text", pushkey_name: "", created_at: ""),
]
