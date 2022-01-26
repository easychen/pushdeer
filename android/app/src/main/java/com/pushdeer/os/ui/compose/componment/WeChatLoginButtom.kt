package com.pushdeer.os.ui.compose.componment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.willowtreeapps.signinwithapplebutton.view.SignInWithAppleButton

@Composable
fun WeChatLoginButton() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .border(BorderStroke(1.dp, color = Color.Green), shape = RoundedCornerShape(4.dp))
            .background(color = Color.Green)
            .width((150).dp)
            .height(38.dp)
    ) {
        Text(text = "aaa")
    }
}

@Preview(showBackground = true)
@Composable
fun W() {
    Row {
        WeChatLoginButton()
        AndroidView(factory = {
            SignInWithAppleButton(it)
        }, modifier = Modifier.border(1.dp, color = Color.Black, shape = RoundedCornerShape(4.dp)))
    }
}