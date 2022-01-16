package com.pushdeer.os

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.util.Linkify
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pushdeer.os.activity.QrScanActivity
import com.pushdeer.os.data.api.data.request.DeviceInfo
import com.pushdeer.os.holder.RequestHolder
import com.pushdeer.os.store.SettingStore
import com.pushdeer.os.ui.compose.page.LogDaoPage
import com.pushdeer.os.ui.compose.page.LoginPage
import com.pushdeer.os.ui.compose.page.main.MainPage
import com.pushdeer.os.ui.theme.PushDeerTheme
import com.pushdeer.os.util.SystemUtil
import com.pushdeer.os.viewmodel.LogDogViewModel
import com.pushdeer.os.viewmodel.MessageViewModel
import com.pushdeer.os.viewmodel.PushDeerViewModel
import com.pushdeer.os.viewmodel.UiViewModel
import com.wh.common.util.UiUtils
import io.noties.markwon.Markwon
import io.noties.markwon.image.coil.CoilImagesPlugin
import io.noties.markwon.linkify.LinkifyPlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : ComponentActivity(), RequestHolder {

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
                crossfade(true)
            }
            .build()
    }

    override val markdown: Markwon by lazy {
        Markwon.builder(this)
            .usePlugin(CoilImagesPlugin.create(this, coilImageLoader))
            .usePlugin(LinkifyPlugin.create(Linkify.WEB_URLS))
            .build();
    }

    override lateinit var globalNavController: NavHostController
    override lateinit var coroutineScope: CoroutineScope
    override lateinit var myActivity: ComponentActivity
    override val clipboardManager by lazy { getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager }

    override lateinit var activityOpener: ActivityResultLauncher<Intent>

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myActivity = this
        activityOpener =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                Toast.makeText(
                    this,
                    "${result.data?.getStringExtra(QrScanActivity.DataKey)}",
                    Toast.LENGTH_SHORT
                ).show()
            }

        UiUtils.keepScreenOn(window)
        setContent {
            globalNavController = rememberNavController()
            coroutineScope = rememberCoroutineScope()
            val useDarkIcons = MaterialTheme.colors.isLight
            val systemUiController = rememberSystemUiController()
            when {
                SystemUtil.isMiui() -> {
                    systemUiController.setStatusBarColor(Color.Transparent, useDarkIcons)
                }
                else -> {
                    systemUiController.setSystemBarsColor(Color.Transparent, useDarkIcons)
                }
            }
            WindowCompat.setDecorFitsSystemWindows(window, true)
            miPushRepository.regId.observe(this) {
                // 这个操作放到注册成功后进行
                settingStore.thisDeviceId = it
                coroutineScope.launch {
                    if (pushDeerViewModel.shouldRegDevice()) {
                        pushDeerViewModel.deviceReg(DeviceInfo().apply {
                            this.name = SystemUtil.getDeviceModel()
                            this.device_id = it
                            this.is_clip = 0
                        })
                    }
                }
            }

            SideEffect {
                coroutineScope.launch {
                    pushDeerViewModel.login().also {
                        pushDeerViewModel.userInfo()
                        pushDeerViewModel.keyList()
                        pushDeerViewModel.deviceList()
                        pushDeerViewModel.messageList()

                        globalNavController.navigate("main") {
                            globalNavController.popBackStack()
                        }
                    }
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
                                LogDaoPage(requestHolder = this@MainActivity)
                            }
                            composable("main") {
                                MainPage(requestHolder = this@MainActivity)
                            }
                        }
                    }
                }
            }
        }
    }
}