package com.pushdeer.os.ui.compose.componment

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.pushdeer.os.holder.RequestHolder
import com.pushdeer.os.ui.theme.SwipeToDismissGray
import com.pushdeer.os.ui.theme.SwipeToDismissRed
import com.pushdeer.os.values.ConstValues
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun SwipeToDismissItem(
    onAction: () -> Unit,
    sidePadding: Boolean = false,
    requestHolder: RequestHolder,
    content: @Composable RowScope.() -> Unit
) {
    var visible by remember {
        mutableStateOf(true)
    }
    val dismissv = DismissValue.Default
    val dismissState = rememberDismissState(initialValue = dismissv)
    LaunchedEffect(Unit){
        if (dismissState.isDismissed(DismissDirection.EndToStart)){
            requestHolder.coroutineScope.launch {
                dismissState.reset()
            }
        }
    }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {

            SwipeToDismiss(
                state = dismissState,
                background = {

                    val color by animateColorAsState(
                        when (dismissState.targetValue) {
                            DismissValue.DismissedToEnd -> SwipeToDismissGray
                            DismissValue.DismissedToStart -> SwipeToDismissRed
                            else -> SwipeToDismissGray
                        }
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                            .background(color)
                            .padding(end = 32.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            requestHolder.coroutineScope.launch {
                                dismissState.reset()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "",
                            )
                        }
                        IconButton(onClick = {
                            visible = false

                            requestHolder.coroutineScope.launch {
                                delay(300)
                                onAction()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "",
                            )
                        }
                    }
                },
                directions = setOf(DismissDirection.EndToStart),
                dismissThresholds = {
                    FractionalThreshold(0.45f)
                },
                dismissContent = content,
                modifier = Modifier.padding(horizontal = if (sidePadding) ConstValues.MainPageSidePadding else 0.dp)
            )
        }

    }
}