package com.pushdeer.os.ui.compose.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pushdeer.os.R
import com.pushdeer.os.holder.RequestHolder
import com.pushdeer.os.ui.theme.MainGreen

@ExperimentalMaterialApi
@Composable
fun LoginPage(requestHolder: RequestHolder) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.logo_com_x2),
            contentDescription = "big push deer logo",
            modifier = Modifier
                .clickable { requestHolder.globalNavController.navigate("logdog") }
                .align(Alignment.TopCenter)
                .padding(top = 50.dp)
        )
        Card(
            onClick = requestHolder.weChatLogin.login,
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .padding(bottom = 220.dp)
                .border(
                    width = 1.dp,
                    color = MainGreen,
                    shape = RoundedCornerShape(4.dp)
                )
                .align(alignment = Alignment.BottomCenter)

        ) {
            Text(
                text = "Sign in with WeChat",
                color = MainGreen,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(0.6F)
            )
        }

        Card(
            onClick = requestHolder.appleLogin.login,
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .padding(bottom = 140.dp)
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(4.dp)
                )
                .align(alignment = Alignment.BottomCenter)

        ) {
            Text(
                text = "Sign in with Apple",
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(0.6F)
            )
        }
    }
}