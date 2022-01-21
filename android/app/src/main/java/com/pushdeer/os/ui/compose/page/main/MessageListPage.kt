package com.pushdeer.os.ui.compose.page.main

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pushdeer.os.R
import com.pushdeer.os.data.database.entity.MessageEntity
import com.pushdeer.os.holder.RequestHolder
import com.pushdeer.os.ui.compose.componment.*
import com.pushdeer.os.ui.navigation.Page
import com.pushdeer.os.ui.theme.MBlue
import com.pushdeer.os.values.ConstValues


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun MessageListPage(requestHolder: RequestHolder) {
    MainPageFrame(
        titleStringId = Page.Messages.labelStringId,
        sideIcon = if (requestHolder.uiViewModel.showMessageSender) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
        onSideIconClick = { requestHolder.toggleMessageSender() },
        sidePadding = false
    ) {
        var s by remember {
            mutableStateOf("")
        }
        val messageList by requestHolder.messageViewModel.all.collectAsState(initial = emptyList())

        LazyColumn(content = {
            item {
                AnimatedVisibility(
                    visible = requestHolder.uiViewModel.showMessageSender,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .padding(horizontal = ConstValues.MainPageSidePadding)

                    ) {
                        OutlinedTextField(
                            value = s,
                            onValueChange = { s = it },
                            shape = RoundedCornerShape(4.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedBorderColor = MaterialTheme.colors.MBlue,
                                focusedLabelColor = Color.Transparent,
                                unfocusedBorderColor = MaterialTheme.colors.MBlue,
                                unfocusedLabelColor = Color.Transparent,
                            ),
                            maxLines = 5,
                            singleLine = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                        )
                        Button(
                            onClick = {
                                requestHolder.messagePushTest(s)
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.MBlue,
                                contentColor = Color.White
                            ),
                        ) {
                            Text(text = stringResource(id = R.string.main_message_send))
                        }
                    }
                }
            }
            items(
                items = messageList,
                key = { item: MessageEntity -> item.id }) { message: MessageEntity ->
                SwipeToDismissItem(
                    onAction = {
                        requestHolder.messageRemove(message.toMessage(), onDone = {
                            requestHolder.messageViewModel.delete(message)
                        })
                    },
                    sidePadding = message.type != "image"
                ) {
                    when (message.type) {
                        "markdown" -> MarkdownMessageItem(message, requestHolder)
                        "text" -> PlainTextMessageItem(message)
                        "image" -> ImageMessageItem(message)
                    }
                }
            }

            item {
                ListBottomBlankItem()
            }
        })

    }
}