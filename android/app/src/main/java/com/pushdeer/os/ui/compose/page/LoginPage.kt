package com.pushdeer.os.ui.compose.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pushdeer.os.R
import com.pushdeer.os.holder.RequestHolder
import com.pushdeer.os.ui.theme.MainBlue
import com.pushdeer.os.ui.theme.MainGreen

@ExperimentalMaterialApi
@Composable
fun LoginPage(requestHolder: RequestHolder) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.logo_com_x2),
            contentDescription = "big push deer logo"
        )
        Card(
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .border(
                    width = 1.dp,
                    color = MainBlue,
                    shape = RoundedCornerShape(4.dp)
                )
        ) {
            Text(
                text = "Sign in with Apple",
                color = MainBlue,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(0.6F)

            )
        }
        Card(
            onClick = {},
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.border(
                width = 1.dp,
                color = MainGreen,
                shape = RoundedCornerShape(4.dp)
            )
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
    }

}