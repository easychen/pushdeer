package com.pushdeer.os.ui.compose.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pushdeer.os.R
import com.pushdeer.os.data.database.entity.LogDog
import com.pushdeer.os.holder.RequestHolder
import com.pushdeer.os.ui.compose.page.main.MainPageFrame

@ExperimentalMaterialApi
@Composable
fun LogDogPage(requestHolder: RequestHolder) {
    MainPageFrame(
        titleStringId = R.string.global_logdog,
        sideIcon = Icons.Default.Delete,
        onSideIconClick = {
            requestHolder.clearLogDog()
        }) {
        val logDogs by requestHolder.logDogViewModel.all.collectAsState(initial = emptyList())

        Scaffold(modifier = Modifier.fillMaxSize()) {
            if (logDogs.isEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_half),
                        contentDescription = "",
                    )
                }
            } else {
                LazyColumn(
                    content = {
                        items(logDogs, key = { item: LogDog -> item.id }) { logDog: LogDog ->
                            Card(
                                onClick = { /*TODO*/ },
                                elevation = 5.dp,
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(text = logDog.toString())
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}