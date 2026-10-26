package com.pushdeer.os.store

import android.content.Context
import com.wh.common.store.Store

class SettingStore(context: Context) {
    val store = Store.create(context, "setting")

    var userToken by store.string("user-token", "")
//    var deviceName by store.string("device-name","My Dear Deer")
//    var useRecv by store.boolean("use-recv",false) // 启用接收
//    var useSend by store.boolean("use-send",false)
//    var useSendNotification by store.boolean("use-send-notification",false)
//    var notificationPackages by store.stringSet("notification-packages", emptySet())
//    var useSendMissedCall by store.boolean("use-send=missed-call",false)
//    var useSendSMS by store.boolean("use-send-sms",false)

    var showMessageSender by store.boolean("show-message-sender", true)
    var thisPushSdk by store.string("this-push-sdk", "mi-push")
    var thisDeviceId by store.string("this-device-id", "")

    var logLevel by store.string("log-level", "i") // i w e - d

    var isServerMethodSelected by store.boolean("server-method-selected", false)
    var isSelfHosted by store.boolean("self-hosted", false)
    var selfHostedEndpointUrl by store.string("self-hosted-endpoint-url", "http://")

    var useInnerWebView by store.boolean("user-inner-webview",false)

    // 保活增强开关：控制 开机自启/网络自愈（默认开启）
    var keepAliveEnabled by store.boolean("keep-alive-enabled", true)

    // 隐藏最近任务开关：App 不出现在最近任务（参考 gkd 方案，默认开启）
    var hideFromRecents by store.boolean("hide-from-recents", true)

    // 轮询间隔（秒）：30/60/180/300/600/1800/3600/10800/21600，默认300（5分钟）
    var pollIntervalSeconds by store.int("poll-interval-seconds", 300)
}