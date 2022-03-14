package com.pushdeer.os

import android.content.*
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pushdeer.os.holder.RequestHolder
import com.pushdeer.os.store.SettingStore
import com.pushdeer.os.ui.compose.componment.MyAlertDialog
import com.pushdeer.os.ui.compose.page.LogDogPage
import com.pushdeer.os.ui.compose.page.LoginPage
import com.pushdeer.os.ui.compose.page.main.MainPage
import com.pushdeer.os.ui.theme.PushDeerTheme
import com.pushdeer.os.util.ActivityOpener
import com.pushdeer.os.util.NotificationUtil
import com.pushdeer.os.util.SystemUtil
import com.pushdeer.os.values.AppKeys
import com.pushdeer.os.viewmodel.LogDogViewModel
import com.pushdeer.os.viewmodel.MessageViewModel
import com.pushdeer.os.viewmodel.PushDeerViewModel
import com.pushdeer.os.viewmodel.UiViewModel
import com.pushdeer.os.wxapi.WXEntryActivity
import com.tencent.mm.opensdk.constants.ConstantsAPI
import io.noties.markwon.*
import io.noties.markwon.image.coil.CoilImagesPlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity(), RequestHolder {

    private val viewModelFactory by lazy { (application as App).viewModelFactory }
    private val repositoryKeeper by lazy { (application as App).repositoryKeeper }
    private val miPushRepository by lazy { repositoryKeeper.miPushRepository }
    override val uiViewModel: UiViewModel by viewModels { viewModelFactory }
    override val pushDeerViewModel: PushDeerViewModel by viewModels { viewModelFactory }
    override val logDogViewModel: LogDogViewModel by viewModels { viewModelFactory }
    override val messageViewModel: MessageViewModel by viewModels { viewModelFactory }
    override val settingStore: SettingStore by lazy { (application as App).storeKeeper.settingStore }

    override val coilImageLoader: ImageLoader by lazy {
        ImageLoader.Builder(this)
            .apply {
                availableMemoryPercentage(0.5)
                bitmapPoolPercentage(0.5)
                crossfade(750)
                allowHardware(true)
            }
            .build()
    }
    override val alert: RequestHolder.AlertRequest by lazy {
        RequestHolder.AlertRequest(resources)
    }
    override val key: RequestHolder.KeyRequest by lazy {
        RequestHolder.KeyRequest(this)
    }
    override val device: RequestHolder.DeviceRequest by lazy {
        RequestHolder.DeviceRequest(this)
    }
    override val message: RequestHolder.MessageRequest by lazy {
        RequestHolder.MessageRequest(this)
    }
    override val clip: RequestHolder.ClipRequest by lazy {
        RequestHolder.ClipRequest(
            getSystemService(
                Context.CLIPBOARD_SERVICE
            ) as ClipboardManager
        )
    }
    override val weChatLogin: RequestHolder.WeChatLoginRequest by lazy {
        RequestHolder.WeChatLoginRequest((application as App).iwxapi)
    }

    override val appleLogin: RequestHolder.AppleLoginRequest by lazy {
        RequestHolder.AppleLoginRequest(supportFragmentManager, this)
    }

    override val markdown: Markwon by lazy {
        Markwon.builder(this)
            .usePlugin(CoilImagesPlugin.create(this, coilImageLoader))
            .usePlugin(object : AbstractMarkwonPlugin() {
                override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
                    builder.linkResolver(object : LinkResolverDef() {
                        override fun resolve(view: View, link: String) {
                            if (settingStore.useInnerWebView){
                                WebViewActivity.load(this@MainActivity, link)
                            }else{
                                super.resolve(view, link)
                            }
                        }
                    })
                }
            })
            .build()
    }

    override lateinit var globalNavController: NavHostController
    override lateinit var coroutineScope: CoroutineScope
    override lateinit var myActivity: AppCompatActivity
    override lateinit var qrScanActivityOpener: ActivityResultLauncher<Intent>
    override lateinit var requestPermissionOpener: ActivityResultLauncher<Array<String>>

    private val wxRegReceiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let {
                    when (it.action) {
                        ConstantsAPI.ACTION_REFRESH_WXAPP -> {
                            weChatLogin.iwxapi.registerApp(AppKeys.WX_Id)
                        }
                        WXEntryActivity.ACTION_RETURN_CODE -> {
                            val code = intent.getStringExtra(WXEntryActivity.CODE_KEY)!!
                            lifecycleScope.launch {
                                if (pushDeerViewModel.userInfo.isAppleLogin) {
                                    Log.d("WH_", "onReceive: isAppleLogin")
                                    // if login, perform merge
                                    coroutineScope.launch {
                                        pushDeerViewModel.userMerge(
                                            "wechat",
                                            code
                                        ) {
                                            coroutineScope.launch {
                                                pushDeerViewModel.userInfo()
                                            }
                                        }
                                    }
                                } else {
                                    Log.d("WH_", "onReceive: plainLogin")
                                    // if not, plain login
                                    coroutineScope.launch {
                                        pushDeerViewModel.loginWithWeiXin(code) {
                                            globalNavController.navigate("main") {
                                                globalNavController.popBackStack()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else -> {
                        }
                    }
                }

            }
        }
    }

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerReceiver(wxRegReceiver,
            IntentFilter().apply {
                addAction(ConstantsAPI.ACTION_REFRESH_WXAPP)
                addAction(WXEntryActivity.ACTION_RETURN_CODE)
            }
        )


        NotificationUtil.setupChannel(this)

        myActivity = this
        qrScanActivityOpener = ActivityOpener.forResult(this)
        requestPermissionOpener = ActivityOpener.forPermission(this)

        setContent {
            globalNavController = rememberNavController()
            coroutineScope = rememberCoroutineScope()
            val useDarkIcons = MaterialTheme.colors.isLight
            val systemUiController = rememberSystemUiController()
            when {
                SystemUtil.isMiui() -> systemUiController.setStatusBarColor(
                    Color.Transparent,
                    useDarkIcons
                )
                else -> systemUiController.setSystemBarsColor(Color.Transparent, useDarkIcons)
            }
            WindowCompat.setDecorFitsSystemWindows(window, true)
            miPushRepository.regId.observe(this) {
                settingStore.thisDeviceId = it
            }

            SideEffect {
                coroutineScope.launch {
                    pushDeerViewModel.loginWithApple(onReturn = {
                        globalNavController.navigate("main") {
                            globalNavController.popBackStack()
                        }
                    })
                }
            }

            PushDeerTheme {
                ProvideWindowInsets {
                    Surface(color = MaterialTheme.colors.background) {
                        NavHost(
                            navController = globalNavController,
                            startDestination = "login",
                            modifier = Modifier.statusBarsPadding()
                        ) {
                            composable("login") {
                                LoginPage(requestHolder = this@MainActivity)
                            }
                            composable("logdog") {
                                LogDogPage(requestHolder = this@MainActivity)
                            }
                            composable("main") {
                                MainPage(requestHolder = this@MainActivity)
                            }
                        }
                    }
                    MyAlertDialog(alertRequest = alert)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(wxRegReceiver)
    }
}