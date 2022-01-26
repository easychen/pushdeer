package com.pushdeer.os.ui.compose.componment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pushdeer.os.ui.theme.MBlue

@ExperimentalMaterialApi
@Composable
fun SettingItem(
    text: String,
    buttonString: String,
    onItemClick: () -> Unit = {},
    paddingValues: PaddingValues = PaddingValues(bottom = 16.dp),
    onButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
    ) {
        CardItemWithContent(onClick = onItemClick) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
//                    .padding(vertical = 10.dp)
                    .padding(start = 20.dp, end = 16.dp, top = 10.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                Button(
                    onClick = onButtonClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.MBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = buttonString,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

//@ExperimentalMaterialApi
//@Composable
//fun SettingItem(content: @Composable () -> Unit) {
//    Card(
//        onClick = onClick,
//        shape = RoundedCornerShape(4.dp),
//        modifier = Modifier
//            .border(
//                width = 1.dp,
//                color = MainBlue,
//                shape = RoundedCornerShape(4.dp)
//            ),
//        content = content,
//        elevation = 5.dp
//    )
//}