package com.pushdeer.os.ui.compose.componment

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pushdeer.os.R
import com.pushdeer.os.ui.theme.MBlue
import com.pushdeer.os.ui.theme.MainBlue

@ExperimentalMaterialApi
@Composable
fun CardItemSingleLineWithIcon(
    onClick: () -> Unit = {},
    @DrawableRes resId: Int = R.drawable.iphone2x,
    text: String = "Easy's iPhone"
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MainBlue,
                shape = RoundedCornerShape(8.dp)
            ),
        elevation = 5.dp

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = resId),
                contentDescription = "",
                colorFilter = ColorFilter.tint(color = MaterialTheme.colors.MBlue),
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = text,
                color = MainBlue,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun CardItemMultiLine(
    onClick: () -> Unit = {},
    @DrawableRes resId: Int = R.drawable.iphone2x,
    text: String = "Easy's iPhone"
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
//            .padding(bottom = 16.dp)
            .border(
                width = 1.dp,
                color = MainBlue,
                shape = RoundedCornerShape(8.dp)
            ),
        elevation = 5.dp

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = resId),
                contentDescription = "",
                colorFilter = ColorFilter.tint(color = MaterialTheme.colors.MBlue),
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = text,
                color = MainBlue,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun CardItemWithContent(onClick: () -> Unit, content: @Composable () -> Unit = {}) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MainBlue,
                shape = RoundedCornerShape(8.dp)
            ),
        content = content,
        elevation = 5.dp
    )
}

@ExperimentalMaterialApi
@Composable
fun CardItemWithContent(content: @Composable () -> Unit = {}) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MainBlue,
                shape = RoundedCornerShape(8.dp)
            ),
        content = content,
        elevation = 5.dp
    )
}

@Composable
fun ListBottomBlankItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
//        Text(text = "End of List",color = Color.Gray)
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun IP() {
    CardItemWithContent {
        Text(text = "aaa")
    }
}