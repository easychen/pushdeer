package com.pushdeer.os.ui.compose.page

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.pushdeer.os.R
import com.pushdeer.os.holder.RequestHolder
import com.willowtreeapps.signinwithapplebutton.SignInWithAppleConfiguration
import com.willowtreeapps.signinwithapplebutton.SignInWithAppleResult
import com.willowtreeapps.signinwithapplebutton.view.SignInWithAppleButton
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun LoginPage(requestHolder: RequestHolder) {
    Box(modifier = Modifier.fillMaxSize()) {

        val configuration = SignInWithAppleConfiguration.Builder()
            .clientId("com.pushdeer.site")
            .redirectUri("https://api2.pushdeer.com/callback/apple")
            .responseType(SignInWithAppleConfiguration.ResponseType.ALL)
            .scope(SignInWithAppleConfiguration.Scope.EMAIL)
            .build()

        Image(
            painter = painterResource(R.drawable.logo_com_x2),
            contentDescription = "big push deer logo",
            modifier = Modifier
                .clickable { requestHolder.globalNavController.navigate("logdog") }
                .align(Alignment.TopCenter)
                .padding(top = 50.dp)
        )
        AndroidView(
            factory = {
                SignInWithAppleButton(it).apply {
                    setUpSignInWithAppleOnClick(
                        requestHolder.fragmentManager,
                        configuration
                    ) { result ->
                        when (result) {
                            is SignInWithAppleResult.Success -> {
                                Log.d("WH_", "apple-id_token:${result.idToken}")
                                requestHolder.coroutineScope.launch {
                                    requestHolder.pushDeerViewModel.login(result.idToken) {
                                        requestHolder.globalNavController.navigate("main") {
                                            requestHolder.globalNavController.popBackStack()
                                        }
                                    }
                                }
                            }
                            is SignInWithAppleResult.Failure -> {
                                requestHolder.alert.alert("Warning Apple Id Login Failed", {
                                    result.error.message
                                }, onOk = {})
                                Log.d(
                                    "WH_",
                                    "Received error from Apple Sign In ${result.error.message}"
                                )
                            }
                            is SignInWithAppleResult.Cancel -> {
                                Log.d("WH_", "User canceled Apple Sign In")
                            }
                        }
                    }
                }
            },
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .padding(bottom = 100.dp)
        )
    }
}