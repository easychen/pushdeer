package com.pushdeer.os.ui.compose.componment

import android.widget.ImageView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.pushdeer.os.R
import com.pushdeer.os.data.api.data.response.PushKey
import com.pushdeer.os.holder.RequestHolder
import com.pushdeer.os.ui.theme.MBlue
import com.wh.common.util.QRCodeGenerator
import com.wh.common.util.TimeUtils

@ExperimentalMaterialApi
@Composable
fun KeyItem(key: PushKey, requestHolder: RequestHolder) {
    var name by remember {
        mutableStateOf(key.name)
    }
    CardItemWithContent(onClick = {
        name = key.name
        requestHolder.alert.alert(
            title = R.string.main_key_alert_changekeyname,
            content = {
                Column {
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        shape = RoundedCornerShape(6.dp),
                        singleLine = true,
                        maxLines = 1,
                        label = { Text(text = stringResource(id = R.string.main_key_alert_keyname)) },
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent,
                        ),
                        trailingIcon = {
                            if (name != "") IconButton(onClick = { name = "" }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "")
                            }
                        }
                    )
                }
            },
            onOk = {
                key.name = name
                requestHolder.key.rename(key)
            }
        )
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_deer_head_with_mail),
                        contentDescription = "",
                        modifier = Modifier.size(36.dp)
                    )
                    Text(
                        text = key.name,
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.MBlue,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 4.dp)
                    )
                    Text(
                        text = TimeUtils.getFormattedTime(
                            TimeUtils.utcTS2ms(
                                key.created_at,
                                "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"
                            ), "MM/dd HH:mm"
                        ),
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 12.sp
                    )
                }
            }
            Text(
                text = key.key,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clickable {
                        requestHolder.alert.alert("QrCode For ${key.name}", {
                            Box(
                                modifier = Modifier.width(400.dp)
                            ) {
                                AndroidView(
                                    factory = {
                                        ImageView(it)
                                    },
                                    update = { view ->
                                        view.setImageBitmap(
                                            QRCodeGenerator(
                                                key.key,
                                                400.dp.value.toInt(),
                                                400.dp.value.toInt()
                                            ).qrCode
                                        )
                                    },
                                    modifier = Modifier.align(alignment = Alignment.Center)
                                )
                            }
                        }, onOk = {})
                    }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = { requestHolder.key.regen(key.id) },
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = MaterialTheme.colors.MBlue
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colors.MBlue),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(text = stringResource(id = R.string.main_key_reset))
                }
                Button(
                    onClick = {
                        requestHolder.clip.copyPushKey(key.key)
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.MBlue,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = stringResource(id = R.string.main_key_copy))
                }
            }
        }

    }
}