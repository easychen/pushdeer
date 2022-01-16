package com.pushdeer.os.ui.compose.page.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pushdeer.os.data.api.data.response.PushKey
import com.pushdeer.os.holder.RequestHolder
import com.pushdeer.os.ui.compose.componment.KeyItem
import com.pushdeer.os.ui.compose.componment.ListBottomBlankItem
import com.pushdeer.os.ui.compose.componment.SwipeToDismissItem
import com.pushdeer.os.ui.navigation.Page

@ExperimentalMaterialApi
@Composable
fun KeyListPage(requestHolder: RequestHolder) {
    MainPageFrame(
        titleStringId = Page.Keys.labelStringId,
        onSideIconClick = { requestHolder.keyGen() }
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(
                requestHolder.pushDeerViewModel.keyList,
                key = { item: PushKey -> item.id }) { pushKey: PushKey ->
                SwipeToDismissItem(onDismiss = { requestHolder.keyRemove(pushKey) }
                ) {
                    KeyItem(key = pushKey, requestHolder = requestHolder)
                }
            }
            item {
                ListBottomBlankItem()
            }
        }
    }
}