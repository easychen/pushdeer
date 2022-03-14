package com.pushdeer.os.util

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import android.util.Log
import com.pushdeer.os.App
import com.pushdeer.os.keeper.RepositoryKeeper
import com.pushdeer.os.values.AppKeys
import com.xiaomi.channel.commonutils.logger.LoggerInterface
import com.xiaomi.mipush.sdk.Logger
import com.xiaomi.mipush.sdk.MiPushClient

object MiPushUtils {

    private fun shouldInitMiPush(context: Context): Boolean {
        val am = context.getSystemService(Application.ACTIVITY_SERVICE) as ActivityManager
        val processInfoList = am.runningAppProcesses
        val mainProcessName = context.applicationInfo.processName
        val myPid = Process.myPid()
        for (info in processInfoList) {
            if (info.pid == myPid && mainProcessName == info.processName) {
                return true
            }
        }
        return false
    }

    fun autoInit(context: Context,repositoryKeeper:RepositoryKeeper){
        if (shouldInitMiPush(context)){
            MiPushClient.registerPush(context, AppKeys.MiPush_Id, AppKeys.MiPush_Key)
        }
        //打开Log
        Logger.setLogger(context, object : LoggerInterface {
            override fun setTag(tag: String) {
                // ignore
            }

            override fun log(content: String, t: Throwable) {
                Log.d(App.TAG, content, t)
                Thread {
                    repositoryKeeper.logDogRepository.log(
                        entity = "mipush",
                        level = "e",
                        event = t.message.toString(),
                        log = content
                    )
                }.start()
            }

            override fun log(content: String) {
                Log.d(App.TAG, content)
//                Thread{
//                    repositoryKeeper.logDogRepository.log(
//                        entity = "mipush",
//                        level = "d",
//                        event = "",
//                        log = content
//                    )
//                }.start()

            }
        })
    }
}