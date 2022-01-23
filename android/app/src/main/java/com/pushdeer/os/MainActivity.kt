package com.pushdeer.os

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.util.Linkify
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
import androidx.fragment.app.FragmentManager
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
import com.pushdeer.os.viewmodel.LogDogViewModel
import com.pushdeer.os.viewmodel.MessageViewModel
import com.pushdeer.os.viewmodel.PushDeerViewModel
import com.pushdeer.os.viewmodel.UiViewModel
import io.noties.markwon.Markwon
import io.noties.markwon.image.coil.CoilImagesPlugin
import io.noties.markwon.linkify.LinkifyPlugin
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
    override val fragmentManager: FragmentManager by lazy { this.supportFragmentManager }

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
        object : RequestHolder.AlertRequest(resources) {}
    }
    override val key: RequestHolder.KeyRequest by lazy {
        object : RequestHolder.KeyRequest(this) {}
    }
    override val device: RequestHolder.DeviceRequest by lazy {
        object : RequestHolder.DeviceRequest(this) {}
    }
    override val message: RequestHolder.MessageRequest by lazy {
        object : RequestHolder.MessageRequest(this) {}
    }
    override val clip: RequestHolder.ClipRequest by lazy {
        object : RequestHolder.ClipRequest(
            getSystemService(
                Context.CLIPBOARD_SERVICE
            ) as ClipboardManager
        ) {}
    }

    override val markdown: Markwon by lazy {
        Markwon.builder(this)
            .usePlugin(CoilImagesPlugin.create(this, coilImageLoader))
            .usePlugin(LinkifyPlugin.create(Linkify.WEB_URLS))
            .build()
    }

    override lateinit var globalNavController: NavHostController
    override lateinit var coroutineScope: CoroutineScope
    override lateinit var myActivity: AppCompatActivity
    override lateinit var qrScanActivityOpener: ActivityResultLauncher<Intent>
    override lateinit var requestPermissionOpener: ActivityResultLauncher<Array<String>>

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                    pushDeerViewModel.login(onReturn = {
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
}