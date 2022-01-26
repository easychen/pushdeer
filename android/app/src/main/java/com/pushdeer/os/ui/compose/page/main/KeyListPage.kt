package com.pushdeer.os.ui.compose.page.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pushdeer.os.R
import com.pushdeer.os.data.api.data.response.PushKey
import com.pushdeer.os.holder.RequestHolder
import com.pushdeer.os.ui.compose.componment.KeyItem
import com.pushdeer.os.ui.compose.componment.ListBottomBlankItem
import com.pushdeer.os.ui.compose.componment.SwipeToDismissItem
import com.pushdeer.os.ui.navigation.Page
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun KeyListPage(requestHolder: RequestHolder) {

    LaunchedEffect(Unit) {
        requestHolder.coroutineScope.launch {
            requestHolder.pushDeerViewModel.keyList()
        }
    }

    MainPageFrame(
        titleStringId = Page.Keys.labelStringId,
        onSideIconClick = { requestHolder.key.gen() }
    ) {
        if(requestHolder.pushDeerViewModel.keyList.isEmpty()){
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.main_key_list_placeholder)
                )
            }
        }else{
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(
                    requestHolder.pushDeerViewModel.keyList.sortedBy { it.id },
                    key = { item: PushKey -> item.id }) { pushKey: PushKey ->
                    SwipeToDismissItem(
                        requestHolder = requestHolder,
                        onAction = { requestHolder.key.remove(pushKey) }
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
}