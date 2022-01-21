package com.pushdeer.os

import android.app.ActivityManager
import android.app.Application
import android.os.Process
import android.util.Log
import com.pushdeer.os.data.api.PushDeerApi
import com.pushdeer.os.data.database.AppDatabase
import com.pushdeer.os.factory.ViewModelFactory
import com.pushdeer.os.keeper.RepositoryKeeper
import com.pushdeer.os.keeper.StoreKeeper
import com.pushdeer.os.values.AppKeys
import com.xiaomi.channel.commonutils.logger.LoggerInterface
import com.xiaomi.mipush.sdk.Logger
import com.xiaomi.mipush.sdk.MiPushClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class App : Application() {

    val storeKeeper by lazy { StoreKeeper(this) }
    val database by lazy { AppDatabase.getDatabase(this) }
    val repositoryKeeper by lazy { RepositoryKeeper(database) }
    val pushDeerService by lazy {
        Retrofit.Builder()
            .baseUrl(PushDeerApi.baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PushDeerApi::class.java)
    }
    val viewModelFactory by lazy {
        ViewModelFactory(
            repositoryKeeper,
            storeKeeper,
            pushDeerService
        )
    }

    override fun onCreate() {
        super.onCreate()
        //初始化push推送服务
        if (shouldInit()) {
            MiPushClient.registerPush(this, AppKeys.MiPush_Id, AppKeys.MiPush_Key)
        }
        //打开Log
        Logger.setLogger(this, object : LoggerInterface {
            override fun setTag(tag: String) {
                // ignore
            }

            override fun log(content: String, t: Throwable) {
                Log.d(TAG, content, t)
            }

            override fun log(content: String) {
                Log.d(TAG, content)
            }
        })
    }

    private fun shouldInit(): Boolean {
        val am = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val processInfoList = am.runningAppProcesses
        val mainProcessName = applicationInfo.processName
        val myPid = Process.myPid()
        for (info in processInfoList) {
            if (info.pid == myPid && mainProcessName == info.processName) {
                return true
            }
        }
        return false
    }

    companion object {
        const val TAG = "TAG"
    }
}
