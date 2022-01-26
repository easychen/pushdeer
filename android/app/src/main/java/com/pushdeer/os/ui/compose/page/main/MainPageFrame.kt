package com.pushdeer.os.ui.compose.page.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pushdeer.os.values.ConstValues

@Composable
fun MainPageFrame(
    titleStringId: Int,
    showSideIcon: Boolean = true,
    sideIcon: ImageVector = Icons.Default.Add,
    iconModifier: Modifier = Modifier,
    onSideIconClick: () -> Unit = {},
    sidePadding: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
//    val sizePaddingValue = if (sidePadding) PaddingValues()
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
//                .padding(horizontal = if (sidePadding) 37.dp else 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, top = 27.dp)
                    .padding(horizontal = ConstValues.MainPageSidePadding)
                    .background(color = Color.Transparent),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = titleStringId), fontSize = 32.sp,
                    fontWeight = FontWeight.W400,
                )
//                if (showSideIcon) {
                    IconButton(onClick = onSideIconClick,modifier = Modifier.alpha(if (showSideIcon)1F else 0F)) {
                        Icon(
                            imageVector = sideIcon,
                            contentDescription = "",
                            tint = Color.LightGray,
                            modifier = iconModifier
                        )
                    }
//                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = if (sidePadding) ConstValues.MainPageSidePadding else 0.dp)
            ) {
                content()
            }
        }
    }
}