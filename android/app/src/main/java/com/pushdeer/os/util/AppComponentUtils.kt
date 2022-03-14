//package com.pushdeer.os.util
//
//import android.content.ComponentName
//import android.content.Context
//import android.content.pm.PackageManager
//import com.pushdeer.os.receiver.MessageReceiver
//import com.xiaomi.mipush.sdk.MessageHandleService
//import com.xiaomi.mipush.sdk.PushMessageHandler
//import com.xiaomi.push.service.XMJobService
//import com.xiaomi.push.service.XMPushService
//import com.xiaomi.push.service.receivers.NetworkStatusReceiver
//import com.xiaomi.push.service.receivers.PingReceiver
//
//object AppComponentUtils {
//
//    fun switchSelfHosted(isSelfHosted: Boolean, context: Context) {
//        val noneSelfHostedComponentNames = noneSelfHostedComponentNames(context)
////        val selfHostedComponentNames = selfHostedComponentNames(context)
//        if (isSelfHosted) {
//            disableComponents(noneSelfHostedComponentNames,context.packageManager)
//            enableComponents(selfHostedComponentNames,context.packageManager)
//        } else {
//            disableComponents(selfHostedComponentNames,context.packageManager)
//            enableComponents(noneSelfHostedComponentNames,context.packageManager)
//        }
//    }
//
//    fun noneSelfHostedComponentNames(context: Context): Array<ComponentName> {
//        return arrayOf(
//            ComponentName(context,XMPushService::class.java),
//            ComponentName(context, XMJobService::class.java),
//            ComponentName(context, PushMessageHandler::class.java),
//            ComponentName(context, MessageHandleService::class.java),
//            ComponentName(context, NetworkStatusReceiver::class.java),
//            ComponentName(context, PingReceiver::class.java),
//            ComponentName(context, MessageReceiver::class.java),
//        )
//    }
//
//    fun enableComponents(componentNames: Array<ComponentName>, pm: PackageManager) {
//        componentNames.forEach {
//            pm.setComponentEnabledSetting(
//                it,
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                PackageManager.DONT_KILL_APP
//            )
//        }
//    }
//
//    fun disableComponents(componentNames: Array<ComponentName>, pm: PackageManager) {
//        componentNames.forEach {
//            pm.setComponentEnabledSetting(
//                it,
//                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                PackageManager.DONT_KILL_APP
//            )
//        }
//    }
//}