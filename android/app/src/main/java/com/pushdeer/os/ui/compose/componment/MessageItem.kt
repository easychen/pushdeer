package com.pushdeer.os.ui.compose.componment

import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.load
import com.pushdeer.os.R
import com.pushdeer.os.data.database.entity.MessageEntity
import com.pushdeer.os.holder.RequestHolder
import com.pushdeer.os.util.CurrentTimeUtil
import com.pushdeer.os.values.ConstValues


@ExperimentalMaterialApi
@Composable
fun PlainTextMessageItem(message: MessageEntity,requestHolder: RequestHolder) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .clickable {
                requestHolder.clip.copyMessagePlainText(message.text)
            }
            .background(color = MaterialTheme.colors.surface)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_deer_head_with_mail),
                contentDescription = "",
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = "${message.pushkey_name}·${
                    CurrentTimeUtil.resolveUTCTimeAndNow(
                        message.created_at,
                        System.currentTimeMillis()
                    )
                }"
            )
        }

        CardItemWithContent {
            Text(
                text = message.text,
                overflow = TextOverflow.Visible,
                lineHeight = 24.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun ImageMessageItem(message: MessageEntity, requestHolder: RequestHolder) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .clickable {
                requestHolder.clip.copyMessagePlainText(message.text)
            }
            .background(color = MaterialTheme.colors.surface)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ConstValues.MainPageSidePadding)
                .padding(bottom = 12.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_deer_head_with_mail),
                contentDescription = "",
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = "${message.pushkey_name}·${
                    CurrentTimeUtil.resolveUTCTimeAndNow(
                        message.created_at,
                        System.currentTimeMillis()
                    )
                }"
            )
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            AndroidView(factory = {
                ImageView(it).apply {
                    scaleType = ImageView.ScaleType.FIT_CENTER
                    load(message.text, requestHolder.coilImageLoader)
                }
            }, modifier = Modifier.fillMaxWidth())
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun MarkdownMessageItem(message: MessageEntity, requestHolder: RequestHolder) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .clickable {
                requestHolder.clip.copyMessagePlainText(message.text)
            }
            .background(color = MaterialTheme.colors.surface)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Image(
                    painter = painterResource(id = R.drawable.ic_deer_head_with_mail),
                    contentDescription = "",
                    modifier = Modifier.size(40.dp)
                )
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_markdown),
//                    contentDescription = "",
//                    tint = MaterialTheme.colors.MBlue,
//                    modifier = Modifier
//                        .size(20.dp)
//                        .align(alignment = Alignment.BottomCenter)
//                )
            }

            Text(
                text = "${message.pushkey_name}·${
                    CurrentTimeUtil.resolveUTCTimeAndNow(
                        message.created_at,
                        System.currentTimeMillis()
                    )
                }"
            )
        }

        CardItemWithContent {
            AndroidView(
                factory = { ctx ->
                    android.widget.TextView(ctx).apply {
                        this.post {
//                            requestHolder.markdown.configuration().theme().
                            requestHolder.markdown.setMarkdown(this, message.text)
                        }
                    }

                }, modifier = Modifier
                    .fillMaxWidth()
//                    .pointerInput(Unit) {
//                        this.detectTapGestures(
//                            onLongPress = {
//                                Log.d("WH_", "MarkdownMessageItem: ")
//                            }
//                        )
//                    }
                    .padding(16.dp)
            )
        }
    }
}