package com.pushdeer.os

import android.app.Application
import com.pushdeer.os.data.api.PushDeerApi
import com.pushdeer.os.data.database.AppDatabase
import com.pushdeer.os.factory.ViewModelFactory
import com.pushdeer.os.keeper.RepositoryKeeper
import com.pushdeer.os.keeper.StoreKeeper
import com.pushdeer.os.util.MiPushUtils
import com.pushdeer.os.values.AppKeys
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class App : Application() {

    val storeKeeper by lazy { StoreKeeper(this) }
    val database by lazy { AppDatabase.getDatabase(this) }
    val repositoryKeeper by lazy { RepositoryKeeper(database, storeKeeper.settingStore) }
    private val pushDeerService: PushDeerApi by lazy {
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

    val iwxapi: IWXAPI by lazy { WXAPIFactory.createWXAPI(this, AppKeys.WX_Id, true) }

    override fun onCreate() {
        super.onCreate()
        //初始化push推送服务
        MiPushUtils.autoInit(this,repositoryKeeper)
    }


    companion object {
        const val TAG = "TAG"
    }
}